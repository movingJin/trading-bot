package movingjin.tradingbot.batch.tickerHistory.domain;

import lombok.Getter;
import lombok.Setter;
import movingjin.tradingbot.bithumApi.Api_Client;
import org.json.JSONObject;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;

import javax.persistence.*;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.HashMap;

import static movingjin.tradingbot.bithumApi.HttpRequest.METHOD_GET;

@Getter
@Setter
@Entity(name = "ticker_history")
@EnableBatchProcessing
public class Ticker {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long idx;

    @Column(nullable = false, name = "time_stamp", columnDefinition = "TIMESTAMP")
    private LocalDateTime timeStamp;

    @Column(name = "coin_name")
    private String coinName;
    private Double open;
    private Double high;
    private Double low;
    private Double close;
    private Double volume;

    public Ticker(){ }

    public Ticker(String coinName) {
        Ticker ticker = null;
        Api_Client api = new Api_Client("dummy", "dummy");
        try {
            String reqUrl = String.format("/public/ticker/%s_KRW", coinName);
            HashMap<String, String> rgParams = new HashMap<String, String>();
            rgParams.put("count", "1");
            String result = api.callApi(reqUrl, rgParams, METHOD_GET);
            JSONObject jObject = new JSONObject(result);
            String status = jObject.getString("status");
            if (status.equals("0000")) {
                JSONObject dataObject = jObject.getJSONObject("data");
                Double opening_price = Double.parseDouble(dataObject.getString("opening_price"));
                Double closing_price = Double.parseDouble(dataObject.getString("closing_price"));
                Double min_price = Double.parseDouble(dataObject.getString("min_price"));
                Double max_price = Double.parseDouble(dataObject.getString("max_price"));
                Double units_traded = Double.parseDouble(dataObject.getString("units_traded"));
                Long date = Long.parseLong(dataObject.getString("date"));
                Instant instant = Instant.ofEpochMilli(date);
                LocalDateTime timeStamp = instant.atZone(ZoneId.systemDefault()).toLocalDateTime();

                this.coinName = coinName;
                this.open = opening_price;
                this.close = closing_price;
                this.high = max_price;
                this.low = min_price;
                this.volume = units_traded;
                this.timeStamp = timeStamp;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
