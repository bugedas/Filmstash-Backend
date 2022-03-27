package com.example.springsocial.api.films;

import com.example.springsocial.api.exceptionHandling.exceptions.CustomDataNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(path = "/api/films")
public class FilmsController {

    private final FilmRepository filmRepository;

    public FilmsController(FilmRepository filmRepository) {
        this.filmRepository = filmRepository;
    }

    @GetMapping
    public List<Film> getFilms() {
        return filmRepository.findAll();
    }

    @GetMapping("/film/id/{id}")
    public Film getFilmById(@PathVariable Long id) {
        return filmRepository.findById(id).orElseThrow(() -> new CustomDataNotFoundException("Film of ID = " + id + " does not exist"));
    }

    @GetMapping("/film/{id}")
    public List<Film> getFilmsByUser(@PathVariable Long id) {
        List<Film> allFilms = filmRepository.findAll();
        List<Film> filmsByUser = new ArrayList<>();
        for (Film f : allFilms
        ) {
            if (f.getUserId().equals(id)) {
                filmsByUser.add(f);
            }
        }
        return filmsByUser;
    }

    @GetMapping("/film/{uid}/{fid}")
    public Film getFilmByUserAndFilm(@PathVariable Long uid, @PathVariable String fid) {
        List<Film> filmsByUser = getFilmsByUser(uid);
        Film film = new Film();
        for (Film f : filmsByUser
        ) {
            if (f.getFilmId().equals(fid)) {
                film = f;
            }
        }
        if (film.getFilmId().equals("")) {
            throw new CustomDataNotFoundException("User of ID = " + uid + " has not added a film of ID = " + fid);
        }
        return film;
    }

    @GetMapping("/list/{uid}/{listName}")
    public List<Film> getFilmByUserAndList(@PathVariable Long uid, @PathVariable String listName) {
        List<Film> filmsByUser = getFilmsByUser(uid);
        List<Film> filteredList = new ArrayList<>();
        for (Film f : filmsByUser
        ) {
            if (f.getListType().equals(listName)) {
                filteredList.add(f);
            }
        }
        if (filteredList.isEmpty()) {
            throw new CustomDataNotFoundException("Films list with a name: " + listName + " is empty or does not exist");
        }
        return filteredList;
    }

    @GetMapping("/list/{uid}")
    public List<String> getFilmLists(@PathVariable Long uid) {
        List<Film> filmsByUser = getFilmsByUser(uid);
        List<String> lists = new ArrayList<>();
        for (Film f : filmsByUser
        ) {
            if (!lists.contains(f.getListType())) {
                lists.add(f.getListType());
            }
        }
        return lists;
    }

    @DeleteMapping("/list/{uid}/{list}")
    public ResponseEntity deleteList(@PathVariable Long id, @PathVariable String list) {
        List<Film> films = getFilmByUserAndList(id, list);
        for(Film f : films) {
            try {
                filmRepository.deleteById(f.getId());
            } catch (Exception e) {
                throw new CustomDataNotFoundException("Film of ID = " + f.getId() + " does not exist");
            }
        }
        return ResponseEntity.ok().build();
    }

    @PostMapping("/film/add")
    public ResponseEntity createFilm(@RequestBody Film film) throws URISyntaxException {
        Film savedFilm = filmRepository.save(film);
        return ResponseEntity.created(new URI("/api/films/" + savedFilm.getId())).body(savedFilm);
    }

    @PutMapping("/film/{id}")
    public ResponseEntity updateFilm(@PathVariable(value = "id") Long id, @RequestBody Film film) {
        Film currentFilm = filmRepository.findById(id).orElseThrow(() -> new CustomDataNotFoundException("Film of ID = " + id + " does not exist"));
        currentFilm.setFilmId(film.getFilmId());
        currentFilm.setUserId(film.getUserId());
        currentFilm.setType(film.getType());
        currentFilm.setUserRated(film.getUserRated());
        currentFilm.setListType(film.getListType());
        final Film updatedFilm = filmRepository.save(currentFilm);

        return ResponseEntity.ok(updatedFilm);
    }

    @DeleteMapping("/film/{id}")
    public ResponseEntity deleteFilm(@PathVariable Long id) {
        try {
            filmRepository.deleteById(id);
        } catch (Exception e) {
            throw new CustomDataNotFoundException("Film of ID = " + id + " does not exist");
        }
        return ResponseEntity.ok().build();
    }
}
