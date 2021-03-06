package audioToText;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;

import com.google.gson.Gson;

public class AudioToText {
	
	private static Transcript transcript = new Transcript();
	private static String jsonRequest;
	
	public static void main(String[] args) throws URISyntaxException, IOException, InterruptedException {
		
		//il file audio deve essere online e accessibile 
		transcript.setAudio("https://github.com/Jok98/API_Exercise/blob/main/Audio/will.m4a?raw=true");
		
		/*
		 * viene utilizzata la libreria di google Gson per convertire l'oggetto transcript in json
		 */
		Gson gson = new Gson();
		jsonRequest = gson.toJson(transcript);
		System.out.println(jsonRequest);
		
		HttpRequest postRequest = (HttpRequest) HttpRequest.newBuilder()
				//inserire l'urrl necessario per effettuare la richiesta
				.uri(new URI("https://api.assemblyai.com/v2/transcript"))
				//header necessario che contiene l'autorizzazione all'uso dell'api tramite key
				.header("Authorization", ApiKey.key)
				/*
				 * prevede come argomento un oggetto di tipo BodyPublishers
				 */
				.POST(BodyPublishers.ofString(jsonRequest))
				.build();
		//inviamo la richiesta tramite la classe HttpClient
		HttpClient httpClient = HttpClient.newHttpClient();
		/*il primo argomento è l'oggetto da inviare, il secondo è
		 *il tipo che ci si aspetta di ricevere come risposta in questo caso una stringa
		 */
		HttpResponse<String> postResponse = httpClient.send(postRequest, BodyHandlers.ofString());
		
		System.out.println(postResponse.body());
		
		transcript = gson.fromJson(postResponse.body(), Transcript.class);
		
		System.out.println(transcript.getId());
		
		//richiesta per ottenere il testo elaborato dall'audio
		HttpRequest getRequest = HttpRequest.newBuilder()
				.uri(new URI("https://api.assemblyai.com/v2/transcript/"+transcript.getId()))
				.header("Authorization", ApiKey.key)
				.build();
		while(true) {
		HttpResponse<String> getResponse = httpClient.send(getRequest,BodyHandlers.ofString());
		transcript = gson.fromJson(getResponse.body(), Transcript.class);
		System.out.println(transcript.getStatus());
		if("completed".equals(transcript.getStatus()) || "error".equals(transcript.getStatus()) ) break;
		Thread.sleep(1000);
		
		}
		System.out.println("Conversione completata");
		System.out.println(transcript.getText());
		
	}

}
