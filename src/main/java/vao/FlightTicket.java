package vao;

import io.quarkus.mongodb.panache.PanacheMongoEntityBase;
import io.quarkus.mongodb.panache.common.MongoEntity;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;

@Data
@NoArgsConstructor
@MongoEntity(collection = "tickets")
public class FlightTicket extends PanacheMongoEntityBase {
/*    public FlightTicket(dto.FlightTicket dto) {
        setProvider(dto.provider());
        setDate(dto.date());
        setCityDeparture(dto.cityDeparture());
        setCityArrival(dto.cityArrival());
    }*/

    public FlightTicket(String provider, String date, String cityDeparture, String cityArrival) {
        this.provider = provider;
        this.date = date;
        this.cityDeparture = cityDeparture;
        this.cityArrival = cityArrival;
    }

/*    public void updateFrom(dto.FlightTicket dto) {
        setProvider(dto.provider());
        setDate(dto.date());
        setCityDeparture(dto.cityDeparture());
        setCityArrival(dto.cityArrival());
    }*/

    public dto.FlightTicket toDto() {
        return new dto.FlightTicket(
                getId(),
                getProvider(),
                getDate(),
                getCityDeparture(),
                getCityArrival());
    }

    @Id
    protected ObjectId id;
    protected String provider;
    protected String date;
    protected String cityDeparture;
    protected String cityArrival;
}
