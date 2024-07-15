package com.rocketseat.planner.links;

import com.rocketseat.planner.activities.ActivityData;
import com.rocketseat.planner.viagem.Trip;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class LinkService {
    @Autowired
    private LinkRepository repository;

    public LinkResponse saveLink(LinkRequestPayLoad payLoad, Trip trip){
        Link newLink = new Link(payLoad.title(), payLoad.url(), trip);

        this.repository.save(newLink);

        return new LinkResponse(newLink.getId());
    }

    public List<LinkData> getAllLinksFromTrip(UUID tripId){
         return this.repository.findByTripId(tripId).stream().map(link -> new LinkData(link.getId(), link.getTitle(), link.getUrl())).toList();
    };
}
