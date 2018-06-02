package nl.robinlaugs.picopizza.stash.api;


import nl.robinlaugs.picopizza.stash.service.StashService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Robin Laugs
 */
@RestController
@RequestMapping("/stash")
public class StashResource {

    private final StashService stashService;

    @Autowired
    public StashResource(StashService stashService) {
        this.stashService = stashService;
    }

    @GetMapping("/{id}")
    public boolean get(@PathVariable String id) {
        return stashService.get(id);
    }

}
