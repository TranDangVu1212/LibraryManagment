package fa.training.services;

import fa.training.entities.Magazine;
import java.util.*;
import java.util.stream.Collectors;

public class MagazineService {
    // In-memory list of Magazines
    private final List<Magazine> magazines = new ArrayList<>();

    public void addMagazine(Magazine magazine) {
        magazines.add(magazine);
        // Re-sort by publication date (ascending)
        magazines.sort(Comparator.comparing(Magazine::getPublicationDate));
    }

    public List<Magazine> getMagazinesByYearAndPublisher(int year, String publisher) {
        return magazines.stream()
                .filter(m -> m.getPublicationYear() == year
                        && m.getPublisher().equalsIgnoreCase(publisher))
                .collect(Collectors.toList());
    }

    public List<Magazine> getTop10ByVolume() {
        return magazines.stream()
                .sorted((m1, m2) -> Integer.compare(m2.getVolume(), m1.getVolume()))
                .limit(10)
                .collect(Collectors.toList());
    }

    public List<Magazine> getAllMagazines() {
        return magazines;
    }
}
