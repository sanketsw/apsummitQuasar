package au.com.ibm.webapp.web.forms;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.apache.commons.io.IOUtils;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.omnifaces.util.Faces;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;

import au.com.ibm.webapp.config.ConfigUtil;
import au.com.ibm.webapp.model.relational.Day;
import au.com.ibm.webapp.model.relational.FileData;
import au.com.ibm.webapp.model.relational.Session;
import au.com.ibm.webapp.service.IFileDataSvc;

@ManagedBean
@ViewScoped
public class ScheduleBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5998416214308538482L;

	public static String NEW_FILE = "schedule_new.xlsx";
	public static String FILE = "schedule.xlsx";

	private List<Day> days = new ArrayList<Day>();

	private String country = null;
	private Map<String, String> countries;

	private UploadedFile file;

	@EJB
	private IFileDataSvc fileDataSvc;

	public void onCountryChange() {
		if (country.equals("")) {
			country = null;
		} else {
			ConfigUtil.setSessionParam("country", country);
		}
	}

	@PostConstruct
	public void Init() {
		try {
			FileData f = fileDataSvc.getById(FILE);
			if (f == null) {
				saveFile(FILE, this.getClass().getResourceAsStream(FILE));
				saveFile(NEW_FILE, this.getClass().getResourceAsStream(NEW_FILE));
			}
			byte[] bytes = fileDataSvc.getById(FILE).getFile();
			InputStream fis = new ByteArrayInputStream(bytes);
			ScheduleManager.getInstance(fis);

		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			for (String id : ScheduleManager.getInstance().getG_dayMap().keySet()) {
				days.add(new Day(id));
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		countries = new HashMap<String, String>();
		countries.put("Team Blue", "Team Blue");
		countries.put("Team Green", "Team Green");
		countries.put("Team Red", "Team Red");
		country = (String) ConfigUtil.getSessionParam("country");
	}

	public UploadedFile getFile() {
		return file;
	}

	public void setFile(UploadedFile file) {
		this.file = file;
	}

	/**
	 * @return the days
	 */
	public List<Day> getDays() {
		return days;
	}

	/**
	 * @param days
	 *            the days to set
	 */
	public void setDays(List<Day> days) {
		this.days = days;
	}

	/**
	 * @return the country
	 */
	public String getCountry() {
		return country;
	}

	/**
	 * @param country
	 *            the country to set
	 */
	public void setCountry(String country) {
		this.country = country;
	}

	/**
	 * @return the countries
	 */
	public Map<String, String> getCountries() {
		return countries;
	}

	/**
	 * @param countries
	 *            the countries to set
	 */
	public void setCountries(Map<String, String> countries) {
		this.countries = countries;
	}

	public List<Session> getSessions(Day day) {
		try {
			return ScheduleManager.getInstance().getSessionsByDayAndTarget(day.getId(), country);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	// public List<Session> getSessionsForChannel(String channelId) {
	// if(channelId == null) return null;
	// return
	// ScheduleManager.getInstance().getChannelSesionsMap().get(channelId);
	// }

	public String getStyleClass(String channel, String target) {
		if (target.contains("Blue")) {
			return "BlueTeam";
		} else if (target.contains("Green")) {
			return "GreenTeam";
		} else if (target.contains("Red")) {
			return "RedTeam";
		}
		return "GraySession";
	}

	public void download() throws IOException, InvalidFormatException {
		byte[] file = downloadOriginalSavedSchedule();
		String date = new SimpleDateFormat("dd-MMM-yy HH:mm")
				.format(Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()));
		String fileName = "Schedule_" + date + ".xlsx";
		try {
			Faces.sendFile(file, fileName, true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void upload(FileUploadEvent event) {
		file = event.getFile();
		if (file != null) {
			// load into db
			try {
				saveFile(NEW_FILE, getFile().getInputstream());
				ScheduleManager.getInstance().loadSchedule(getFile().getInputstream());

				FacesMessage message = new FacesMessage("Good to go!", "Schedule is uploaded.");
				FacesContext.getCurrentInstance().addMessage("growl", message);
			} catch (Exception e) {
				e.printStackTrace();
				FacesContext.getCurrentInstance().addMessage("growl", new FacesMessage("Oops!",
						"Failed to load the new schedule. " + "Check the file formatting again."));
			}
		}
	}

	public void save() {
		try {
			saveSchedule();
		} catch (Exception e) {
			e.printStackTrace();
			FacesContext.getCurrentInstance().addMessage("growl", new FacesMessage("Oops!",
					"Problem saving new schedule file. It is active on the microsite, but you won't be able to download the new version."));
		}
		FacesContext.getCurrentInstance().addMessage("growl",
				new FacesMessage("Cool", "The new schedule has been saved."));
	}

	public void revert() {
		try {
			loadOriginalSavedSchedule();
		} catch (Exception e) {
			e.printStackTrace();
			FacesContext.getCurrentInstance().addMessage("growl", new FacesMessage("Ohh Ohh!",
					"Can't reverse back. Try uploading corerctly formatted schedule instead."));
		}
		FacesContext.getCurrentInstance().addMessage("growl",
				new FacesMessage("No problem", "You are back to your previous schedule."));
	}

	private void loadOriginalSavedSchedule() throws Exception {
		byte[] bytes = fileDataSvc.getById(FILE).getFile();
		InputStream fis = new ByteArrayInputStream(bytes);
		ScheduleManager.getInstance().loadSchedule(fis);
	}

	private void saveFile(String name, InputStream fis) throws IOException {
		byte[] bytes = IOUtils.toByteArray(fis);
		saveFile(name, bytes);
	}

	private void saveFile(String name, byte[] bytes) throws IOException {
		FileData f = fileDataSvc.getById(name);
		if (f == null) {
			f = new FileData();
			f.setName(name);
		}
		f.setFile(bytes);
		fileDataSvc.update(f);
	}

	public byte[] downloadOriginalSavedSchedule() throws IOException {
		byte[] bytes = fileDataSvc.getById(FILE).getFile();
		return bytes;
	}

	public void saveSchedule() throws Exception {
		byte[] bytes = fileDataSvc.getById(NEW_FILE).getFile();
		saveFile(FILE, bytes);
	}
}
