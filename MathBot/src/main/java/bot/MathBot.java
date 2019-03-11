package bot;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;

public class MathBot extends TelegramLongPollingBot {

	@Override
	public void onUpdateReceived(final Update update) {
		String messageTextReceived = update.getMessage().getText();

		final long chatId = update.getMessage().getChatId();

		ScriptEngineManager mgr = new ScriptEngineManager();
		ScriptEngine engine = mgr.getEngineByName("JavaScript");
		String text = "";

		if (messageTextReceived.equals("/start")) {
			text = "Hello, welcome to this small project of  a bot.\nMy father is @Gonza116, and this is very early in "
					+ "development.\nType /help for help.";
		} else if (messageTextReceived.equals("/help")) {
			text = "This bot always returns a `double` value. It uses a Javascript engine so doing math operations here "
					+ "takes the same syntax.\nSum: A + B\nSubstraction: A - B\nMulitiplication: A * B\nDivision: A / B\nModulus ("
					+ "division reminder): A % B\nLogic comparators: A == B, A > B... (0.0 = false, 1.0 = true)";
		} else {
			messageTextReceived = "1.0 * (" + messageTextReceived + ")";
			try {
				text = String.valueOf((Double) engine.eval(messageTextReceived));
			} catch (ScriptException e1) {
				text = "Sorry, I couldn't understand you";
			}
		}

		SendMessage message = new SendMessage().setChatId(chatId).setText(text);

		try {
			execute(message);
		} catch (TelegramApiException e) {
			e.printStackTrace();
		}
	}

	@Override
	public String getBotUsername() {
		//here you return your bot username;
	}

	@Override
	public String getBotToken() {
		//here you return your bot token;
	}
}
