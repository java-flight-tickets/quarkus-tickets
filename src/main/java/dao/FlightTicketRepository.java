package dao;

import io.quarkus.mongodb.panache.PanacheMongoRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import vao.FlightTicket;

@ApplicationScoped
public class FlightTicketRepository  implements PanacheMongoRepository<FlightTicket> {
}
