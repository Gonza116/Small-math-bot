package bot;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import org.telegram.telegrambots.api.methods.AnswerInlineQuery;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.api.objects.inlinequery.inputmessagecontent.InputTextMessageContent;
import org.telegram.telegrambots.api.objects.inlinequery.result.InlineQueryResult;
import org.telegram.telegrambots.api.objects.inlinequery.result.InlineQueryResultArticle;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;

public class MathBot extends TelegramLongPollingBot {

	@Override
	public void onUpdateReceived(final Update update) {
		String messageTextReceived;
		Long chatId = null;
		if(update.getInlineQuery() != null) {
			//This handles inline queries
			messageTextReceived = update.getInlineQuery().getQuery();
		} else {
			//This handles usual 1-by-1 queries
			messageTextReceived = update.getMessage().getText();
			chatId = update.getMessage().getChatId();
		}

		//Here we summon the Javascript maths engine
		ScriptEngineManager mgr = new ScriptEngineManager();
		ScriptEngine engine = mgr.getEngineByName("JavaScript");
		String text = "";

		if (messageTextReceived.equals("/start")) {
			//This is the first message the user sees
			text = "Hello, welcome to this small project of  a bot.\nMy father is @Gonza116, and this is very early in "
					+ "development.\nType /help for help.";
		} else if (messageTextReceived.equals("/help")) {
			//This is the help documentation
			text = "This bot always returns a double value. It uses a Javascript engine so doing math operations here "
					+ "takes the same syntax.\nSum: A + B\nSubstraction: A - B\nMulitiplication: A * B\nDivision: A / B\nModulus ("
					+ "division reminder): A % B\nLogic comparators: A == B, A > B... (0.0 = false, 1.0 = true)\nPower (work in progress):"
					+ " A ^ B";
		} else {
			//This is where the magic happens
			if(messageTextReceived.contains("^")) {
				try {
					//As Javascript doesn't have a String to power converter, I made one myselg
					//TODO: Do the same with all operators
					messageTextReceived = this.toPower(messageTextReceived);
				} catch(Exception e) {
					e.printStackTrace();
					messageTextReceived = "";
				}
			}
			//Here we convert whatever result we will get in a double
			messageTextReceived = "1.0 * (" + messageTextReceived.replaceAll(",", ".") + ")";
			try {
				//We evaluate the string and try to make it a number
				text = String.valueOf((Double) engine.eval(messageTextReceived));
			} catch (ScriptException e1) {
				Double number = Math.random() * 100;
				if(number >= 99) {
					//This is a silly easter egg
					text = "Que te calles, demonio de los cojones";
				} else {
					//This is your usual "please type right" message
					text = "Sorry, I couldn't understand you";
				}

			}
		}

		if(text.equals("NaN")) {
			text = "Sorry, I couldn't understand you";
		}

		if(update.getInlineQuery() != null) {
			//This handles inline queries
			AnswerInlineQuery ans = new AnswerInlineQuery();
			ans.setInlineQueryId(update.getInlineQuery().getId());
			List<InlineQueryResult> results = new ArrayList<>();
			InlineQueryResultArticle res = new InlineQueryResultArticle();
			res.setId("0");
			res.setTitle(text);
			InputTextMessageContent itext = new InputTextMessageContent();
			itext.disableWebPagePreview();
			itext.setMessageText(text);
			res.setInputMessageContent(itext);
			results.add(res);
			ans.setResults(results);
			try {
				execute(ans);
			} catch (TelegramApiException e) {
				e.printStackTrace();
			}
		} else {
			//This sends a message to the private conversation between a bot and a user
			SendMessage message = new SendMessage().setChatId(chatId).setText(text);
			try {
				execute(message);
			} catch (TelegramApiException e) {
				e.printStackTrace();
			}
		}




	}


	private String toPower(String messageTextReceived) {
		String[] values = messageTextReceived.trim().split("\\^");
		Double base = Double.valueOf(values[0].trim());
		Double power = Double.valueOf(values[1].trim());
		Double res = Math.pow(base, power);
		return String.valueOf(res);
	}

	@Override
	public String getBotUsername() {
		return "Small_math_bot";
	}

	@Override
	public String getBotToken() {
		//Here goes the token;
	}
}
