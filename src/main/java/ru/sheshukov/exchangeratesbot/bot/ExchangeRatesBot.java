package ru.sheshukov.exchangeratesbot.bot;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.data.mongo.MongoReactiveDataAutoConfiguration;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.sheshukov.exchangeratesbot.exception.ServiceException;
import ru.sheshukov.exchangeratesbot.service.ExchangeRatesService;

import java.awt.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

@Component
public class ExchangeRatesBot extends TelegramLongPollingBot {

    private static final Logger LOG = LoggerFactory.getLogger(ExchangeRatesBot.class);


    private static final String START = "/start";
    private static final String USD = "/usd";
    private static final String EUR = "/eur";
    private static final String GBP = "/gbp";
    private static final String CNY = "/cny";
    private static final String AUD = "/aud";
    private static final String CALC = "/calc";


    @Autowired
    private ExchangeRatesService exchangeRatesService;





    public ExchangeRatesBot(@Value("${bot.token}") String botToken) {
        super(botToken);
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (!update.hasMessage() || !update.getMessage().hasText()) {
            return;
        }
        var message = update.getMessage().getText();
        var chatId = update.getMessage().getChatId();
        switch (message) {

            case START :
                String userName = update.getMessage().getChat().getUserName();
                startCommand(chatId, userName);
                break;
            case USD:
                usdCommand(chatId);
                break;
            case EUR:
                eurCommand(chatId);
                break;
            case CNY:
                cnyCommand(chatId);
                break;
            case AUD:
                audCommand(chatId);
                break;
            case GBP:
                gbpCommand(chatId);
                break;
            case CALC:

                break;

            default:
                unknownCommand(chatId);
       }
    }

    @Override
    public String getBotUsername() {
        return "sheshukov_exchange_rates_bot";
    }

    private void startCommand(Long chatId, String userName) {
        var text = """
                
                Добро пожаловать в CurrenciesBot, %s❗
                
                Здесь Вы сможете узнать официальные курсы валют на сегодня, установленные ЦБ РФ 🏦.
                
                💵Команды валют💵
                /usd - курс доллара $
                /eur - курс евро €
                /cny - курс китайский юань ¥
                /gbp - курс британского фунта £
                /aud - курс австралийского доллара AU$
                
                """;
        var formattedText = String.format(text, userName);

        sendMessage(chatId, formattedText);
    }


    private void usdCommand(Long chatId) {
        String formattedText;
        try {
            var currency = exchangeRatesService.getUSDExchangeRate();
            var text = """ 
              
              📅 на момент  %s 📅
              
              💵 1 Доллар США 💵
                      
              ₽ составляет %s рублей ₽""" ;

            formattedText = String.format(text, LocalDate.now(), currency);
        } catch (ServiceException e) {
            LOG.error("Ошибка получения курса доллара", e);
            formattedText = "Не удалось получить текущий курс доллара США. Попробуйте позже.";
        }
        sendMessage(chatId, formattedText);
    }

    private void eurCommand(Long chatId) {
        String formattedText;
        try {
            var  currency = exchangeRatesService.getEURExchangeRate();
            var text = """ 
              
              📅 на момент  %s 📅
              
              💵 1 Евро 💵
                      
              ₽ составляет %s рублей ₽""";
            formattedText = String.format(text, LocalDate.now(), currency);
        } catch (ServiceException e) {
            LOG.error("Ошибка получения курса евро", e);
            formattedText = "Не удалось получить текущий курс евро. Попробуйте позже.";
        }
        sendMessage(chatId, formattedText);
    }
    public void cnyCommand(Long chatId) {

            String formattedText;
            try {
                var currency = exchangeRatesService.getCNYExchangeRate();
                var text = """ 
              
              📅 на момент  %s 📅
              
              💵 1 Юань 💵
                      
              ₽ составляет %s рублей ₽""";
                formattedText = String.format(text, LocalDate.now(), currency);
            } catch (ServiceException e) {
                LOG.error("Ошибка получения курса доллара", e);
                formattedText = "Не удалось получить текущий курс юань. Попробуйте позже.";
            }
            sendMessage(chatId, formattedText);

    }
    public void gbpCommand(Long chatId) {

        String formattedText;
        try {
            var currency = exchangeRatesService.getGBPExchangeRate();
            var text = """ 
              
              📅 на момент  %s 📅
              
              💵 1 Британский фунт 💵
                      
              ₽ составляет %s рублей ₽""";
            formattedText = String.format(text, LocalDate.now(), currency);
        } catch (ServiceException e) {
            LOG.error("Ошибка получения курса британский фунт", e);
            formattedText = "Не удалось получить текущий курс британского фунта. Попробуйте позже.";
        }
        sendMessage(chatId, formattedText);

    }
    public void audCommand(Long chatId) {

        String formattedText;
        try {
            var currency = exchangeRatesService.getAUDExchangeRate();
            var text = """ 
              
              📅 на момент  %s 📅
              
              💵 1 Австралийский доллар 💵
                      
              ₽ составляет %s рублей ₽""";
            formattedText = String.format(text, LocalDate.now(), currency);
        } catch (ServiceException e) {
            LOG.error("Ошибка получения курса доллара", e);
            formattedText = "Не удалось получить текущий курс австралийского доллара. Попробуйте позже.";
        }
        sendMessage(chatId, formattedText);

    }
    private void unknownCommand(Long chatId) {
        var text = """
               Не удалось распознать команду!
                
               💵Команды валют💵
                /usd - курс доллара $
                /eur - курс евро €
                /cny - курс китайский юань ¥
                /gbp - курс британского фунта £
                /aud - курс австралийского доллара AU$
                      
                """;;
        sendMessage(chatId, text);
    }


    private void sendMessage(Long chatId, String text) {
        var chatIdStr = String.valueOf(chatId);
        var sendMessage = new SendMessage(chatIdStr, text);

        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        List<KeyboardRow> keyboardRows = new ArrayList<>();
        KeyboardRow rows = new KeyboardRow();
        rows.add(new KeyboardButton("/usd"));
        rows.add(new KeyboardButton("/eur"));
        keyboardRows.add(rows);
        KeyboardRow row = new KeyboardRow();
        row.add(new KeyboardButton("/cny"));
        row.add(new KeyboardButton("/gbp"));
        row.add(new KeyboardButton("/aud"));
        keyboardRows.add(row);
        replyKeyboardMarkup.setKeyboard(keyboardRows);
        sendMessage.setReplyMarkup(replyKeyboardMarkup);


        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            LOG.error("Ошибка отправки сообщения", e);
        }
    }
}
