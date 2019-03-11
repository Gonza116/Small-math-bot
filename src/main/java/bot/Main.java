package bot;

import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.TelegramBotsApi;
import org.telegram.telegrambots.exceptions.TelegramApiException;

public class Main {

	public static void main(String[] args) {
		ApiContextInitializer.init();
 
		final TelegramBotsApi telegramBotsApi = new TelegramBotsApi();
 
		try {
			telegramBotsApi.registerBot(new MathBot());
		} catch (TelegramApiException e) {
			e.printStackTrace();
		}
	}
}
