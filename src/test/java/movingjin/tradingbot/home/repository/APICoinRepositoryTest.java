package movingjin.tradingbot.home.repository;

import movingjin.tradingbot.bithumApi.Api_Client;
import movingjin.tradingbot.home.domain.Coin;
import movingjin.tradingbot.trading.domain.Order;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.*;

import static movingjin.tradingbot.bithumApi.HttpRequest.METHOD_GET;

public class APICoinRepositoryTest {
    private static int order_count = 0;
    private static final Map<String, Coin> store = new LinkedHashMap<>();
    private Double balance = 50000000.0;
    Logger logger = LoggerFactory.getLogger(getClass());

    public APICoinRepositoryTest()
    {
        for(Coin.Token token: Coin.Token.values()) {
            Coin coin = new Coin(token.name());
            store.put(token.name(), coin);
        }
    }

    public List<Coin> findAll(String connectKey, String secretKey) {
        for(int u=0; u<Coin.Token.values().length; u++) {
            String coinName = Coin.Token.values()[u].name();
            Coin coin = getCoinInfoFromApi(connectKey, secretKey, coinName);

            store.put(coinName, coin);
        }

        return new ArrayList<>(store.values());
    }

    public Optional<Coin> findByCoinName(String connectKey, String secretKey, String coinName) {
        Coin coin = getCoinInfoFromApi(connectKey, secretKey, coinName);
        store.put(coinName, coin);

        return Optional.ofNullable(coin);
    }

    public String marketBidding(String coinName, Double price, Double quantity) {
        String order_id = "" + order_count++;
        balance -= (price * quantity);
        return order_id;
    }

    public String marketSell(String coinName, Double price, Double quantity) {
        String order_id = "" + order_count++;
        balance += (price * quantity);
        return order_id;
    }

    public Order getOrderDetail(String coinName, String order_id, Double price, Double quantity) {
        LocalDateTime now = LocalDateTime.now();
        Order order = new Order(order_id, now, "test_key", coinName, price, quantity);
        return order;
    }

    public Double getBalance()
    {
        return this.balance;
    }

    public Double getCurrentPriceByCoin(String coinName) {
        Double currentPrice = 0.0;
        Api_Client api = new Api_Client("dummy", "dummy");

        try {
            {
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
