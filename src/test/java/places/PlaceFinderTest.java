package places;

import model.PlaceFinder;
import model.PlaceLocation;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

public class PlaceFinderTest {
    static String[] arr = {"BAR DE CERVEZAS", "BAR DE TAPAS", "UNIVERSIDAD DE BELGRANO",
            "UNIVERSIDAD CATOLICA ARGENTINA", "MUSEO NACIONAL DEL ARTE",
            "MUSEO DE LAS ARMAS", "BOQUE"
    };
    @Test
    void testPlaceFinder() {
        int i = 0;
        List<PlaceLocation> places = new ArrayList<>();
        for (String s : arr) {
            PlaceLocation aux = new PlaceLocation(s, i, i);
            i++;
            places.add(aux);
        }
        PlaceFinder finder = new PlaceFinder(places);
        List<PlaceLocation> similar = finder.findPlaces("BAR");
        String[] toCompare = {"BAR DE TAPAS", "BAR DE CERVEZAS", "BOQUE",
                "MUSEO DE LAS ARMAS", "MUSEO NACIONAL DEL ARTE",
                "UNIVERSIDAD CATOLICA ARGENTINA", "UNIVERSIDAD DE BELGRANO"
        };
        Assertions.assertArrayEquals(similar.stream().map(PlaceLocation::getName).toArray(), toCompare);
        similar = finder.findPlaces("UNIVERSIDAD");
        toCompare = new String[]{"UNIVERSIDAD DE BELGRANO", "UNIVERSIDAD CATOLICA ARGENTINA",
                "BAR DE CERVEZAS", "BAR DE TAPAS", "BOQUE",
                "MUSEO DE LAS ARMAS", "MUSEO NACIONAL DEL ARTE",
        };
        Assertions.assertArrayEquals(similar.stream().map(PlaceLocation::getName).toArray(), toCompare);
    }
}
