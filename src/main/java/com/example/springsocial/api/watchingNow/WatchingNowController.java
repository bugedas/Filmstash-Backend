package com.example.springsocial.api.watchingNow;

import com.example.springsocial.api.exceptionHandling.exceptions.CustomDataNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(path = "/api/watching-now")
public class WatchingNowController {

    private final WatchingNowRepository watchingNowRepository;

    public WatchingNowController(WatchingNowRepository watchingNowRepository) {
        this.watchingNowRepository = watchingNowRepository;
    }

    @GetMapping
    public List<WatchingNow> getWatchingNow() {
        return watchingNowRepository.findAll();
    }

    @GetMapping("/id/{id}")
    public WatchingNow getWatchingNowById(@PathVariable Long id) {
        return watchingNowRepository.findById(id).orElseThrow(() -> new CustomDataNotFoundException("Tv of ID = " + id + " does not exist"));
    }

    @GetMapping("/{id}")
    public List<WatchingNow> getWatchingNowByUser(@PathVariable Long id) {
        List<WatchingNow> allWatchingNow = watchingNowRepository.findAll();
        List<WatchingNow> watchingNowByUser = new ArrayList<>();
        for (WatchingNow f : allWatchingNow
        ) {
            if (f.getUserId().equals(id)) {
                watchingNowByUser.add(f);
            }
        }
        return watchingNowByUser;
    }

    @GetMapping("/{uid}/{tvid}")
    public WatchingNow getWatchingNowByUserAndTv(@PathVariable Long uid, @PathVariable String tvid) {
        List<WatchingNow> watchingNowByUser = getWatchingNowByUser(uid);
        WatchingNow watchingNow = new WatchingNow();
        for (WatchingNow f : watchingNowByUser
        ) {
            if (f.getTvId().equals(tvid)) {
                watchingNow = f;
            }
        }
        if (watchingNow.getTvId().equals("")) {
            throw new CustomDataNotFoundException("User of ID = " + uid + " is not watching a tv of ID = " + tvid);
        }
        return watchingNow;
    }

    @PostMapping("/add")
    public ResponseEntity createWatchingNow(@RequestBody WatchingNow watchingNow) throws URISyntaxException {
        WatchingNow savedWatchingNow = watchingNowRepository.save(watchingNow);
        return ResponseEntity.created(new URI("/api/watching-now/" + savedWatchingNow.getId())).body(savedWatchingNow);
    }

    @PutMapping("/{id}")
    public ResponseEntity updateWatchingNow(@PathVariable(value = "id") Long id, @RequestBody WatchingNow watchingNow) {
        WatchingNow currentWatchingNow = watchingNowRepository.findById(id).orElseThrow(() -> new CustomDataNotFoundException("Tv of ID = " + id + " does not exist"));
        currentWatchingNow.setTvId(watchingNow.getTvId());
        currentWatchingNow.setUserId(watchingNow.getUserId());
        currentWatchingNow.setSeasonNr(watchingNow.getSeasonNr());
        currentWatchingNow.setEpisodeNr(watchingNow.getEpisodeNr());
        currentWatchingNow.setFinished(watchingNow.getFinished());
        currentWatchingNow.setStillWatching(watchingNow.getStillWatching());
        final WatchingNow updatedFilm = watchingNowRepository.save(currentWatchingNow);

        return ResponseEntity.ok(updatedFilm);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteWatchingNow(@PathVariable Long id) {
        try {
            watchingNowRepository.deleteById(id);
        } catch (Exception e) {
            throw new CustomDataNotFoundException("WatchingNow of ID = " + id + " does not exist");
        }
        return ResponseEntity.ok().build();
    }
}
