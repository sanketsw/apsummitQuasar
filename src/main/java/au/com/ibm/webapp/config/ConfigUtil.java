package au.com.ibm.webapp.config;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import javax.el.ELContext;
import javax.faces.FacesException;
import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.imageio.ImageIO;

import org.imgscalr.Scalr;

public class ConfigUtil {

	public static String getSavedUrl() {
		/*
		 * FIXME: Need to resolve why this isn't woring HttpServletRequest
		 * request = ((HttpServletRequest)
		 * FacesContext.getCurrentInstance().getExternalContext().getRequest());
		 * 
		 * SavedRequest savedRequest = new
		 * HttpSessionRequestCache().getRequest(request, (HttpServletResponse)
		 * FacesContext.getCurrentInstance()
		 * .getExternalContext().getResponse());
		 * 
		 * if (savedRequest != null) { try { URL url = new
		 * URL(savedRequest.getRedirectUrl()); return
		 * url.getFile().substring(request.getContextPath().length()); } catch
		 * (Exception e) { //log.error(e.getMessage() + " Using default URL"); }
		 * }
		 */
		return "index.xhtml"; // default page!
	}

	public static void growl(String summary, String message) {
		FacesContext context = FacesContext.getCurrentInstance();
		context.addMessage("growl", new FacesMessage(summary, message));
	}

	public static void growl(String summary, String message, Severity severity) {
		FacesContext context = FacesContext.getCurrentInstance();
		context.addMessage("growl", new FacesMessage(severity, summary, message));
	}

	public static byte[] createThumbnail(InputStream is) throws IOException {
		BufferedImage image = ImageIO.read(is); // load image
		BufferedImage thumbnail = Scalr.resize(image, Scalr.Method.SPEED, 150, 150, Scalr.OP_ANTIALIAS);

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ImageIO.write(thumbnail, "jpg", baos);
		byte[] bytes = baos.toByteArray();
		return bytes;
	}

	public static Object getManagedBean(final String beanName) {
		FacesContext fc = FacesContext.getCurrentInstance();
		Object bean;

		try {
			ELContext elContext = fc.getELContext();
			bean = elContext.getELResolver().getValue(elContext, null, beanName);
		} catch (RuntimeException e) {
			throw new FacesException(e.getMessage(), e);
		}

		if (bean == null) {
			throw new FacesException("Managed bean with name '" + beanName
					+ "' was not found. Check your faces-config.xml or @ManagedBean annotation.");
		}

		return bean;
	}

	public static void setSessionParam(String key, Object value) {
		ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
		Map<String, Object> sessionMap = externalContext.getSessionMap();
		sessionMap.put(key, value);
	}

	public static Object getSessionParam(String key) {
		ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
		Map<String, Object> sessionMap = externalContext.getSessionMap();
		return sessionMap.get(key);

	}
}
