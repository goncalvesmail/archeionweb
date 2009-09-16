package br.com.archeion.mbean;

import java.io.PrintWriter;
import java.io.StringWriter;

import javax.servlet.ServletException;

public class ExceptionManagedBean {

	private Throwable exc;

	public void setExc(Throwable exc) {
		this.exc = exc;
	}

    public String getStackTrace() {
        StringWriter writer = new StringWriter();
        PrintWriter pw = new PrintWriter(writer);
        fillStackTrace(this.exc, pw);
        return writer.toString();
    }

    private void fillStackTrace(Throwable ex, PrintWriter pw) {
        if (null == ex) {
            return;
        }

        ex.printStackTrace(pw);

        if (ex instanceof ServletException) {
            Throwable cause = ((ServletException) ex).getRootCause();

            if (null != cause) {
                pw.println("Root Cause:");
                fillStackTrace(cause, pw);
            }
        } else {
            Throwable cause = ex.getCause();

            if (null != cause) {
                pw.println("Cause:");
                fillStackTrace(cause, pw);
            }
        }
    }
}
