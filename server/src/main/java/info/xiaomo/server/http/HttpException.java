package info.xiaomo.server.http;

/**
 * Created on 2016/12/10 18:08.
 *
 * @author 周锟
 */
public class HttpException extends RuntimeException {
	private int status;

	public HttpException(String message, int status) {
		super(message);
		this.status = status;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}
}
