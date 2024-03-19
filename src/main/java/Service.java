

import dao.FlightTicketRepository;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import io.quarkus.grpc.GrpcService;
import org.bson.types.ObjectId;
import org.eventTicket.TicketOuterClass;
import org.eventTicket.TicketServiceGrpc;
import vao.FlightTicket;

import java.util.logging.Logger;

@GrpcService
public class Service extends TicketServiceGrpc.TicketServiceImplBase {
    public FlightTicketRepository dao;

    public Service() {
        this.dao = new FlightTicketRepository();
    }
    private static final Logger log = Logger.getLogger(Service.class.toString());

    @Override
    public void createTicket(TicketOuterClass.Ticket request, StreamObserver<TicketOuterClass.Ticket> responseObserver) {
        try {
            FlightTicket ticket = new FlightTicket(
                    request.getProvider(),
                    request.getDate(),
                    request.getCityDeparture(),
                    request.getCityArrival()
            );

            dao.persist(ticket);

            log.info(() -> "Ticket created");

            TicketOuterClass.Ticket response = TicketOuterClass.Ticket.newBuilder()
                    .setProvider(ticket.getProvider())
                    .setDate(ticket.getDate())
                    .setCityDeparture(ticket.getCityDeparture())
                    .setCityArrival(ticket.getCityArrival())
                    .build();
            responseObserver.onNext(response);
            responseObserver.onCompleted();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void getAllTickets(TicketOuterClass.Empty request, StreamObserver<TicketOuterClass.TicketList> responseObserver) {
        log.info("Fetching all tickets");
        TicketOuterClass.TicketList.Builder ticketListBuilder = TicketOuterClass.TicketList.newBuilder();
        dao.listAll().forEach(ticket -> {
            TicketOuterClass.Ticket.Builder grpcTicketBuilder = TicketOuterClass.Ticket.newBuilder()
                    .setId(ticket.getId().toString())
                    .setProvider(ticket.getProvider())
                    .setDate(ticket.getDate())
                    .setCityDeparture(ticket.getCityDeparture())
                    .setCityArrival(ticket.getCityArrival());

            if (ticket.getId() != null) {
                grpcTicketBuilder.setId(ticket.getId().toString());
            }

            ticketListBuilder.addTickets(grpcTicketBuilder.build());
        });

        responseObserver.onNext(ticketListBuilder.build());
        responseObserver.onCompleted();
    }



    @Override
    public void getTicketById(TicketOuterClass.TicketRequestId request, StreamObserver<TicketOuterClass.Ticket> responseObserver) {
        try {
            String id = request.getId();
            ObjectId personId = new ObjectId(id);
            FlightTicket ticket = dao.findById(personId);

            if (ticket != null) {
                TicketOuterClass.Ticket response = TicketOuterClass.Ticket.newBuilder()
                        .setId(ticket.getId().toString())
                        .setProvider(ticket.getProvider())
                        .setDate(ticket.getDate())
                        .setCityDeparture(ticket.getCityDeparture())
                        .setCityArrival(ticket.getCityArrival())
                        .build();

                responseObserver.onNext(response);
                responseObserver.onCompleted();
            } else {
                responseObserver.onError(Status.NOT_FOUND.withDescription("Ticket not found").asRuntimeException());
            }
        } catch (Exception e) {
            responseObserver.onError(Status.INTERNAL.withDescription("Failed to retrieve ticket").withCause(e).asRuntimeException());
        }
    }

    @Override
    public void removeTicket(TicketOuterClass.TicketRequestId request, StreamObserver<TicketOuterClass.Empty> responseObserver) {
        try {
            String id = request.getId();
            ObjectId personId = new ObjectId(id);
            FlightTicket ticket = dao.findById(personId);

            if (ticket != null) {
                dao.delete(ticket);
            } else {
                responseObserver.onError(Status.NOT_FOUND.withDescription("Ticket not found").asRuntimeException());
            }
        } catch (Exception e) {
            responseObserver.onError(Status.INTERNAL.withDescription("Failed to retrieve ticket").withCause(e).asRuntimeException());
        }
    }

}
