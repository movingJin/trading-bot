package movingjin.tradingbot.home.repository;

import movingjin.tradingbot.bithumApi.Api_Client;
import movingjin.tradingbot.home.domain.Coin;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.*;

import static movingjin.tradingbot.bithumApi.HttpRequest.METHOD_GET;
import static movingjin.tradingbot.bithumApi.HttpRequest.METHOD_POST;

public class APICoinRepository implements CoinInterface {
    private static Double balance = null;
    private static final Map<String, Coin> store = new LinkedHashMap<>();

    public APICoinRepository()
    {
        for(Coin.Token token: Coin.Token.values()) {
            Coin coin = new Coin(token.name());
            store.put(token.name(), coin);
        }

        /*
        Coin btc = new Coin("BTC", 36000000.0, 12500000.0, 1.0);
        Coin eth = new Coin("ETH", 1100000.0, 210000.0, 10.0);
        Coin xrp = new Coin("XRP", 4000.0, 505.0, 25000.0);

        store.put("BTC", btc);
        store.put("ETH", eth);
        store.put("XRP", xrp);
         */
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
            System.out.println(result);
            JSONObject jObject = new JSONObject(result);
            String status = jObject.getString("status");
            if (status.equals("0000")) {
                JSONObject dataObject = jObject.getJSONObject("data");
                balance = Double.parseDouble(dataObject.getString("available_krw"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return balance;
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
                    coin.setMarketPrice(Double.parseDouble(openPrice));
                }
            }

            {   //Getting and setting quantity
                String reqUrl = String.format("/info/balance");
                HashMap<String, String> rgParams = new HashMap<String, String>();
                rgParams.put("currency", coinName);
                String result = api.callApi(reqUrl, rgParams, METHOD_POST);
                JSONObject jObject = new JSONObject(result);
                String status = jObject.getString("status");
                if (status.equals("0000")) {
                    String quantityKey = String.format("total_%s", coinName.toLowerCase());
                    JSONObject dataObject = jObject.getJSONObject("data");
                    balance = Double.parseDouble(dataObject.getString("available_krw"));
                    String quantity = dataObject.getString(quantityKey);
                    coin.setQuantity(Double.parseDouble(quantity));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return coin;
    }
}
