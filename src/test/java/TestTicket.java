import dao.FlightTicketRepository;
import io.grpc.stub.StreamObserver;
import io.quarkus.test.junit.QuarkusTest;
import org.eventTicket.TicketOuterClass;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import vao.FlightTicket;

import javax.inject.Inject;
import java.util.function.Supplier;
import java.util.logging.Logger;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@QuarkusTest
public class TestTicket {

    private static final Logger log = Logger.getLogger(TestTicket.class.getName());

    @Mock
    private FlightTicketRepository flightTicketRepository;

    @InjectMocks
    private Service ticketService;

    @Test
    public void testCreateTicket_Success() {
        // Initialize mocks
        MockitoAnnotations.openMocks(this);

        // Mocking input and output
        TicketOuterClass.Ticket request = TicketOuterClass.Ticket.newBuilder()
                .setId("test123")
                .setProvider("Provider1")
                .setDate("2024-03-19")
                .setCityDeparture("City1")
                .setCityArrival("City2")
                .build();
        log.info(String.valueOf(request));
        StreamObserver<TicketOuterClass.Ticket> responseObserver = mock(StreamObserver.class);

        ticketService.createTicket(request, responseObserver);

        // Verify the response
        verify(responseObserver).onNext(any());
        verify(responseObserver).onCompleted();
        verify(responseObserver, never()).onError(any(Throwable.class));
    }
}
