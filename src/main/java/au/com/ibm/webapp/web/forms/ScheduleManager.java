/**
 * 
 */
package au.com.ibm.webapp.web.forms;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.openjpa.kernel.SaveFieldManager;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import au.com.ibm.webapp.model.relational.FileData;
import au.com.ibm.webapp.model.relational.Presenter;
import au.com.ibm.webapp.model.relational.Schedule;
import au.com.ibm.webapp.model.relational.Session;
import au.com.ibm.webapp.service.IFileDataSvc;

/**
 * @author sanketsw
 *
 */
public class ScheduleManager {


	/**
	 * Make this a singleton class
	 */
	private static ScheduleManager inst = null;

	/**
	 * Map of day number with sessions on that day
	 */
	private Map<String, List<Session>> g_dayMap = null;
	/**
	 * Map of session id to session
	 */
	private Map<String, Session> g_sessionMap = null;

	/**
	 * Name to Presenter details
	 */
	private Map<String, Presenter> g_presenterMap = null;

	private ScheduleManager() {
	}

	public static ScheduleManager getInstance() throws Exception {
		if (inst == null) {
			throw new Exception("ScheduleManager has not been instantiated");
		}
		return inst;
	}


	public void loadSchedule(InputStream fis) throws Exception {
		Map<String, List<Session>> dayMap = new HashMap<String, List<Session>>();
		Map<String, Session> sessionMap = new HashMap<String, Session>();
		Map<String, Presenter> presenterMap = new HashMap<>();
		// Map<String, List<Session>> channelSesionsMap = new HashMap<String,
		// List<Session>>();
		System.out.println("Loading sessions data...");

		// read from excel file
		if (fis == null) {
			throw new Exception("input stream is null");
		}
		// System.out.println(fis);
		OPCPackage pkg = OPCPackage.open(fis);
		@SuppressWarnings("resource")
		XSSFWorkbook wb = new XSSFWorkbook(pkg);

		// Open the First sheet
		Sheet sheet = wb.getSheetAt(0);
		int count = 0;
		for (Row row : sheet) {
			// eat blank line at the top of the sheet
			if ((row.getCell(0) == null) || StringUtils.isBlank(row.getCell(0).toString())) {
				continue;
			}
			// skip the header row
			if (count < 1) {
				count++;
				continue;
			}

			Schedule t = new Schedule();
			String day = getCellvalue(row, 0);
			String title = getCellvalue(row, 2);
			String channelNumber = getCellvalue(row, 7);
			Session s = sessionMap.get(Session.getSessionId(title, channelNumber));
			if (s == null) {
				s = new Session();
			}
			t.setDay(day);
			t.setTime(getCellvalue(row, 1));
			s.setTitle(title);
			s.setPresenter(getCellvalue(row, 3));
			s.setExec(getCellvalue(row, 4));
			s.setDescription(getCellvalue(row, 5));
			t.setTarget(getCellvalue(row, 6));
			s.setParent(channelNumber); // channel number
			t.setRoom(getCellvalue(row, 8));
			s.setURL(getCellvalue(row, 9));
			s.setChannel(s.getParent() != null ? true : false);
			s.add(t);
			s.setId(Session.getSessionId(title, channelNumber));

			insert(dayMap, day, s);
			sessionMap.put(s.getId(), s);
		}

		// Read email addresses
		sheet = wb.getSheetAt(1);
		count = 0;
		for (Row row : sheet) {
			// eat blank line at the top of the sheet
			if ((row.getCell(0) == null) || StringUtils.isBlank(row.getCell(0).toString())) {
				continue;
			}
			// skip the header row
			if (count < 1) {
				count++;
				continue;
			}

			Presenter p = new Presenter();
			p.setName(getCellvalue(row, 0));
			p.setDesignation(getCellvalue(row, 1));
			p.setEmail(getCellvalue(row, 2));
			presenterMap.put(p.getName(), p);

		}

		// Read the channels
		/*
		 * Not required anymore for (int i = 1; i < 4; i++) { sheet =
		 * wb.getSheetAt(i); count = 0; for (Row row : sheet) { // eat blank
		 * line at the top of the sheet if ((row.getCell(0) == null) ||
		 * StringUtils.isBlank(row.getCell(0).toString())) { continue; } // skip
		 * the header row if (count < 1) { count++; continue; }
		 * 
		 * Session s = new Session(); s.setId(row.getCell(0).toString());
		 * s.setStartTime(getCellvalue(row, 1)); s.setTitle(getCellvalue(row,
		 * 2)); s.setDescription(getCellvalue(row, 3));
		 * s.setStructure(getCellvalue(row, 4));
		 * s.setPresenter(getCellvalue(row, 5)); s.setTarget(getCellvalue(row,
		 * 6)); s.setParent(Integer.toString(i)); s.setChannel(false);
		 * insert(channelSesionsMap, Integer.toString(i), s);
		 * sessionMap.put(s.getId(), s); } }
		 */
		getInstance().setG_dayMap(dayMap);
		getInstance().setG_sessionMap(sessionMap);
		getInstance().setG_presenterMap(presenterMap);

	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private static void insert(Map map, Object key, Session s) {
		List<Session> l = (List<Session>) map.get(key);
		if (l == null) {
			l = new ArrayList<Session>();
			map.put(key, l);
		}
		l.add(s);
	}

	private static String getCellvalue(Row row, int i) {
		if ((row.getCell(i) == null) || StringUtils.isBlank(row.getCell(i).toString()))
			return null;
		else {
			String val = row.getCell(i).toString();
			try {
				float fVal = Float.parseFloat(val);
				int intVal = (int) fVal;
				// System.out.println(intVal);
				val = Integer.toString(intVal);
			} catch (Exception e) {
				;
				;
			}
			// System.out.println(val);
			return val;
		}
	}

	public List<Session> getSessionsByDayAndTarget(String day, String target) {
		List<Session> l = getG_dayMap().get(day);
		List<Session> retList = new ArrayList<Session>();
		for (Session s : l) {
			if (s.getSchedule(day, target) != null) {
				retList.add(s);
			}
		}
		return retList;
	}

	/*-- Getters and Setters --*/

	public Map<String, List<Session>> getG_dayMap() {
		return g_dayMap;
	}

	public void setG_dayMap(Map<String, List<Session>> g_dayMap) {
		this.g_dayMap = g_dayMap;
	}

	public Map<String, Session> getG_sessionMap() {
		return g_sessionMap;
	}

	public void setG_sessionMap(Map<String, Session> g_sessionMap) {
		this.g_sessionMap = g_sessionMap;
	}

	public Map<String, Presenter> getG_presenterMap() {
		return g_presenterMap;
	}

	public void setG_presenterMap(Map<String, Presenter> g_presenterMap) {
		this.g_presenterMap = g_presenterMap;
	}

	public static ScheduleManager getInstance(InputStream fis) throws Exception {
		if(inst == null) {
			inst = new ScheduleManager();
			inst.loadSchedule(fis);
		}
		return inst;
	}

}
