package pjatk.s18617.tournamentmanagement.services;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import pjatk.s18617.tournamentmanagement.controllers.NotFoundException;
import pjatk.s18617.tournamentmanagement.dtos.LocationCreationDto;
import pjatk.s18617.tournamentmanagement.model.Location;
import pjatk.s18617.tournamentmanagement.model.Tournament;
import pjatk.s18617.tournamentmanagement.model.User;
import pjatk.s18617.tournamentmanagement.repositories.LocationRepository;
import pjatk.s18617.tournamentmanagement.repositories.TournamentRepository;

@RequiredArgsConstructor
@Service
public class LocationServiceImpl implements LocationService {

    private final LocationRepository locationRepository;
    private final UserService userService;
    private final TournamentRepository tournamentRepository;

    @Override
    public void checkAuthorization(Tournament tournament, String username) {
        User user = userService.findByUsername(username).orElseThrow(NotFoundException::new);
        checkAuthorization(tournament, user);
    }

    @Override
    public void checkAuthorization(Tournament tournament, User user) {
        boolean userIsNotAdmin = !user.isAdmin();
        boolean userIsNotOwner = !user.equals(tournament.getUserOwner());
        boolean cannotManageLocation = userIsNotAdmin && userIsNotOwner;
        if (cannotManageLocation)
            throw new AccessDeniedException("Nie masz praw do zarządzania lokalizacją tego turnieju.");
    }

    @Override
    public Location saveWithAuthorization(LocationCreationDto locationCreationDto, Tournament tournament,
                                          String username) {
        User user = userService.findByUsername(username).orElseThrow(NotFoundException::new);
        checkAuthorization(tournament, user);

        Location newLocation = Location.builder()
                .country(locationCreationDto.getCountry())
                .postalCode(locationCreationDto.getPostalCode())
                .city(locationCreationDto.getCity())
                .street(locationCreationDto.getStreet())
                .houseNumber(locationCreationDto.getHouseNumber())
                .build();

        tournament.setLocation(newLocation);
        tournamentRepository.save(tournament); // also saves Location because of Cascade.ALL relationship

        return tournament.getLocation();
    }

    @Override
    public void deleteWithAuthorization(Tournament tournament, String username) {
        User user = userService.findByUsername(username).orElseThrow(NotFoundException::new);
        checkAuthorization(tournament, user);

        tournament.setLocation(null);
        tournamentRepository.save(tournament); // also deletes Location because of Cascade.ALL relationship
    }

    @Override
    public Location updateWithAuthorization(Tournament tournament, LocationCreationDto locationCreationDto,
                                            String username) {
        User user = userService.findByUsername(username).orElseThrow(NotFoundException::new);
        checkAuthorization(tournament, user);

        Location location = tournament.getLocation();
        location.setCountry(locationCreationDto.getCountry());
        location.setPostalCode(locationCreationDto.getPostalCode());
        location.setCity(locationCreationDto.getCity());
        location.setStreet(locationCreationDto.getStreet());
        location.setHouseNumber(locationCreationDto.getHouseNumber());
        return locationRepository.save(location);
    }

}
