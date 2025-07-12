package pjatk.s18617.tournamentmanagement.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import pjatk.s18617.tournamentmanagement.dtos.LocationCreationDto;
import pjatk.s18617.tournamentmanagement.model.Location;
import pjatk.s18617.tournamentmanagement.model.Tournament;
import pjatk.s18617.tournamentmanagement.services.LocationService;
import pjatk.s18617.tournamentmanagement.services.TournamentService;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
public class LocationController {

    private final TournamentService tournamentService;
    private final LocationService locationService;

    @PostMapping("/tournament/{tournamentId}/delete/location")
    public String deleteLocation(@PathVariable Long tournamentId, Principal principal) {
        Tournament tournament = tournamentService.getById(tournamentId).orElseThrow(NotFoundException::new);

        String username = principal.getName();

        locationService.deleteWithAuthorization(tournament, username);
        return "redirect:/tournament/" + tournamentId;
    }

    @GetMapping("/tournament/{tournamentId}/new/location")
    public String showLocationCreationForm(@PathVariable Long tournamentId, Model model, Principal principal) {
        Tournament tournament = tournamentService.getById(tournamentId).orElseThrow(NotFoundException::new);

        String username = principal.getName();
        locationService.checkAuthorization(tournament, username);

        model.addAttribute("locationCreationDto", new LocationCreationDto());
        model.addAttribute("tournament", tournament);

        return "tournament/tournament-location-add";
    }

    @PostMapping("/tournament/{tournamentId}/new/location")
    public String processLocationCreationForm(@PathVariable Long tournamentId, Model model, Principal principal,
                                              @Valid LocationCreationDto locationCreationDto, BindingResult result) {
        Tournament tournament = tournamentService.getById(tournamentId).orElseThrow(NotFoundException::new);

        if (result.hasErrors()) {
            model.addAttribute("tournament", tournament);
            return "tournament/tournament-location-add";
        }

        String username = principal.getName();
        locationService.saveWithAuthorization(locationCreationDto, tournament, username);
        return "redirect:/tournament/" + tournamentId;
    }

    @GetMapping("/tournament/{tournamentId}/edit/location")
    public String showLocationEditForm(@PathVariable Long tournamentId, Model model, Principal principal) {
        Tournament tournament = tournamentService.getById(tournamentId).orElseThrow(NotFoundException::new);
        Location location = tournament.getLocation();

        String username = principal.getName();
        locationService.checkAuthorization(tournament, username);

        LocationCreationDto locationCreationDto = new LocationCreationDto(
                location.getCountry(),
                location.getPostalCode(),
                location.getCity(),
                location.getStreet(),
                location.getHouseNumber()
        );
        model.addAttribute("locationCreationDto", locationCreationDto);
        model.addAttribute("tournament", tournament);
        return "tournament/tournament-location-edit";
    }

    @PostMapping("/tournament/{tournamentId}/edit/location")
    public String processLocationEditForm(@PathVariable Long tournamentId, Model model, Principal principal,
                                          @Valid LocationCreationDto locationCreationDto, BindingResult result) {
        Tournament tournament = tournamentService.getById(tournamentId).orElseThrow(NotFoundException::new);
        String username = principal.getName();

        if (result.hasErrors()) {
            model.addAttribute("tournament", tournament);
            return "tournament/tournament-location-edit";
        }

        locationService.updateWithAuthorization(tournament, locationCreationDto, username);
        return "redirect:/tournament/" + tournamentId;
    }

}
