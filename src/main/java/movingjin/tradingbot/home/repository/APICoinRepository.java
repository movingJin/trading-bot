package movingjin.tradingbot.home.repository;

import movingjin.tradingbot.bithumApi.Api_Client;
import movingjin.tradingbot.home.domain.Coin;
import movingjin.tradingbot.trading.domain.Order;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

import static movingjin.tradingbot.bithumApi.HttpRequest.METHOD_GET;
import static movingjin.tradingbot.bithumApi.HttpRequest.METHOD_POST;

public class APICoinRepository implements CoinInterface {
    private static final Map<String, Coin> store = new LinkedHashMap<>();
    Logger logger = LoggerFactory.getLogger(getClass());

    public APICoinRepository()
    {
        for(Coin.Token token: Coin.Token.values()) {
            Coin coin = new Coin(token.name());
            store.put(token.name(), coin);
        }
    }

    @Override
    public List<Coin> findAll(String connectKey, String secretKey) {
        for(int u=0; u<Coin.Token.values().length; u++) {
            String coinName = Coin.Token.values()[u].name();
            Coin coin = getCoinInfoFromApi(connectKey, secretKey, coinName);

            store.put(coinName, coin);
        }

        return new ArrayList<>(store.values());
    }

    @Override
    public Optional<Coin> findByCoinName(String connectKey, String secretKey, String coinName) {
        Coin coin = getCoinInfoFromApi(connectKey, secretKey, coinName);
        store.put(coinName, coin);

        return Optional.ofNullable(coin);
    }

    @Override
    public String marketBidding(String connectKey, String secretKey, String coinName, Double quantity) {
        String order_id = null;
        Api_Client api = new Api_Client(connectKey, secretKey);

        try {
            {   //Getting and setting market price
                String reqUrl = "/trade/market_buy";
                HashMap<String, String> rgParams = new HashMap<String, String>();
                rgParams.put("order_currency", coinName);
                rgParams.put("units", "" + quantity);
                rgParams.put("payment_currency", "KRW");
                String result = api.callApi(reqUrl, rgParams, METHOD_POST);
                JSONObject jObject = new JSONObject(result);
                String status = jObject.getString("status");
                if (status.equals("0000")) {
                    order_id = jObject.getString("order_id");
                }
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return order_id;
    }

    @Override
    public Order getOrderDetail(String connectKey, String secretKey, String coinName, String order_id) {
        Order order = null;
        Api_Client api = new Api_Client(connectKey, secretKey);
        try {
            {   //Getting and setting market price
                String reqUrl = "/info/order_detail";
                HashMap<String, String> rgParams = new HashMap<String, String>();
                rgParams.put("order_currency", coinName);
                rgParams.put("order_id", "" + order_id);
                rgParams.put("payment_currency", "KRW");
                String result = api.callApi(reqUrl, rgParams, METHOD_POST);
                logger.debug(result);
                JSONObject jObject = new JSONObject(result);
                String status = jObject.getString("status");
                if (status.equals("0000")) {
                    JSONObject dataObject = jObject.getJSONObject("data");
                    String order_status = dataObject.getString("order_status");
                    if(order_status.equals("Completed"))
                    {
                        Long date = Long.parseLong(dataObject.getString("order_date"));
                        Instant instant = Instant.ofEpochMilli(date);
                        LocalDateTime transaction_date = instant.atZone(ZoneId.systemDefault()).toLocalDateTime();
                        String order_currency = dataObject.getString("order_currency");
                        JSONArray contractArray = dataObject.getJSONArray("contract");
                        Double acc = 0.0;
                        Iterator<Object> iterator = contractArray.iterator();
                        while (iterator.hasNext()) {
                            JSONObject jb = (JSONObject) iterator.next();
                            Double price = Double.parseDouble(jb.getString("price"));
                            acc += price;
                        }
                        Double order_qty = Double.parseDouble(dataObject.getString("order_qty"));

                        order = new Order(transaction_date, connectKey, order_currency, acc, order_qty);
                    }
                    else
                    {
                        String log_message = String.format("order_id :%s is not Completed." ,order_id);
                        logger.info(log_message);
                    }
                }
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return order;
    }

    @Override
    public Double getBalance(String connectKey, String secretKey)
    {
        Double balance = null;
        Api_Client api = new Api_Client(connectKey, secretKey);
        try
        {   //Getting and setting quantity
            String reqUrl = String.format("/info/balance");
            HashMap<String, String> rgParams = new HashMap<String, String>();
            rgParams.put("currency", "BTC");
            String result = api.callApi(reqUrl, rgParams, METHOD_POST);
            JSONObject jObject = new JSONObject(result);
            String status = jObject.getString("status");
            if (status.equals("0000")) {
                JSONObject dataObject = jObject.getJSONObject("data");
                balance = Double.parseDouble(dataObject.getString("available_krw"));
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
        }

        return balance;
    }

    @Override
    public Double getCurrentPriceByCoin(String coinName) {
        Double currentPrice = 0.0;
        Api_Client api = new Api_Client("dummy", "dummy");

        try {
            {   //Getting and setting market price
                String reqUrl = String.format("/public/orderbook/%s_KRW", coinName);
                HashMap<String, String> rgParams = new HashMap<String, String>();
                rgParams.put("count", "1");
                String result = api.callApi(reqUrl, rgParams, METHOD_GET);
                JSONObject jObject = new JSONObject(result);
                String status = jObject.getString("status");
                if (status.equals("0000")) {
                    JSONObject dataObject = jObject.getJSONObject("data");
                    JSONArray bidsArray = dataObject.getJSONArray("bids");
                    String openPrice = bidsArray.getJSONObject(0).getString("price");
                    currentPrice = Double.parseDouble(openPrice);
                }
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return currentPrice;
    }

    private Coin getCoinInfoFromApi(String connectKey, String secretKey, String coinName)
    {
        Coin pickCoin = store.values().stream()
                .filter(coin -> coin.getName().equals(coinName))
                .findAny().get();

        Coin coin = new Coin(coinName);
        coin.setIsRun(pickCoin.getIsRun());
        Api_Client api = new Api_Client(connectKey, secretKey);

        try {
            {   //Getting and setting market price
                Double currentPrice = getCurrentPriceByCoin(coinName);
                coin.setMarketPrice(currentPrice);
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return coin;
    }
}
