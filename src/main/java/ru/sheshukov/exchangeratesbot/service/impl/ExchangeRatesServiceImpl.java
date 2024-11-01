package ru.sheshukov.exchangeratesbot.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import ru.sheshukov.exchangeratesbot.client.CbrClient;
import ru.sheshukov.exchangeratesbot.exception.ServiceException;
import ru.sheshukov.exchangeratesbot.service.ExchangeRatesService;

import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.StringReader;

@Service
public class ExchangeRatesServiceImpl implements ExchangeRatesService {

    private static final Logger LOG = LoggerFactory.getLogger(ExchangeRatesServiceImpl.class);

    private static final String USD_XPATH = "/ValCurs//Valute[@ID='R01235']/Value";
    private static final String EUR_XPATH = "/ValCurs//Valute[@ID='R01239']/Value";
    private static final String CNY_XPATH = "/ValCurs//Valute[@ID='R01375']/Value";
    private static final String AUD_XPATH = "/ValCurs//Valute[@ID='R01010']/Value";
    private static final String GBP_XPATH = "/ValCurs//Valute[@ID='R01035']/Value";

    @Autowired
    private CbrClient client;

    @Cacheable(value = "usd", unless = "#result == null or #result.isEmpty()")
    @Override
    public String getUSDExchangeRate() throws ServiceException {
        var xmlOptional = client.getCurrencyRatesXML();
        String xml = xmlOptional.orElseThrow(
                () -> new ServiceException("Не удалось получить XML")
        );
        return extractCurrencyValueFromXML(xml, USD_XPATH);
    }

    @Cacheable(value = "eur", unless = "#result == null or #result.isEmpty()")
    @Override
    public String getEURExchangeRate() throws ServiceException {
        var xmlOptional = client.getCurrencyRatesXML();
        String xml = xmlOptional.orElseThrow(
                () -> new ServiceException("Не удалось получить XML")
        );
        return extractCurrencyValueFromXML(xml, EUR_XPATH);
    }
    @Cacheable(value = "cny", unless = "#result == null or #result.isEmpty()")
    @Override
    public String getCNYExchangeRate() throws ServiceException {
        var xmlOptional = client.getCurrencyRatesXML();
        String xml = xmlOptional.orElseThrow(
                () -> new ServiceException("Не удалось получить XML")
        );
        return extractCurrencyValueFromXML(xml, CNY_XPATH);
    }
    @Cacheable(value = "aud")
    @Override
    public String getAUDExchangeRate() throws ServiceException {
        var xmlOptional = client.getCurrencyRatesXML();
        String xml = xmlOptional.orElseThrow(
                () -> new ServiceException("Не удалось получить XML")
        );
        return extractCurrencyValueFromXML(xml, AUD_XPATH);
    }
    @Cacheable(value = "gbp", unless = "#result == null or #result.isEmpty()")
    @Override
    public String getGBPExchangeRate() throws ServiceException {
        var xmlOptional = client.getCurrencyRatesXML();
        String xml = xmlOptional.orElseThrow(
                () -> new ServiceException("Не удалось получить XML")
        );
        return extractCurrencyValueFromXML(xml, GBP_XPATH);
    }

    @CacheEvict("usd")
    @Override
    public void clearUSDCache() {
        LOG.info("Cache \"usd\" cleared!");
    }

    @CacheEvict("eur")
    @Override
    public void clearEURCache() {
        LOG.info("Cache \"eur\" cleared!");
    }

    @CacheEvict("cny")
    @Override
    public void clearCNYCache() {
        LOG.info("Cache \"cny\" cleared!");
    }
    @CacheEvict("cad")
    @Override
    public void clearAUDCache() {
        LOG.info("Cache \"aud\" cleared!");
    }
    @CacheEvict("gbp")
    @Override
    public void clearGBPCache() {
        LOG.info("Cache \"gbp\" cleared!");
    }

    private static String extractCurrencyValueFromXML(String xml, String xpathExpression)
            throws ServiceException {
        var source = new InputSource(new StringReader(xml));
        try {
            var xpath = XPathFactory.newInstance().newXPath();
            var document = (Document) xpath.evaluate("/", source, XPathConstants.NODE);

            return xpath.evaluate(xpathExpression, document);
        } catch (XPathExpressionException e) {
            throw new ServiceException("Не удалось распарсить XML", e);
        }
    }
}
