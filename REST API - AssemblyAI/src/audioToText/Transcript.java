package audioToText;

public class Transcript {
	
	private String audio_url;
	private String id;
	private String status;
	private String text;
	
	public String getAudio() {
		return audio_url;
	}

	public void setAudio(String audio) {
		this.audio_url = audio;
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

}
