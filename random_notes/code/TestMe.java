import org.junit.Test;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TestMe {

   private static class SomeThing {
      private final int id;
      private final String thing;

      public SomeThing(int id, String thing) {
         this.id = id;
         this.thing = thing;
      }

      public int getId() {
         return id;
      }

      public String getThing() {
         return thing;
      }

      @Override
      public boolean equals(Object obj) {
         return obj instanceof SomeThing && id == ((SomeThing) obj).id;
      }

      @Override
      public int hashCode() {
         return id;
      }

      @Override
      public String toString() {
         return String.format("(%s, %s)", id, thing);
      }
   }

   @Test
   public void testMe() {
      List<SomeThing> one = new ArrayList<>();
      one.add(new SomeThing(1, "thingOne"));
      one.add(null);
      one.add(new SomeThing(3, "thingThree"));


      List<SomeThing> two = new ArrayList<>();
      one.add(new SomeThing(2, "thingTwo"));
      one.add(null);
      one.add(new SomeThing(3, "thingThree"));

      List<SomeThing> uniqueNonNullThings = Stream.concat(one.stream(), two.stream())
         .filter(Objects::nonNull)
         .distinct()
         .sorted(Comparator.comparing(SomeThing::getId)) // optionally sort 
         .collect(Collectors.toList());

      System.out.println(uniqueNonNullThings);
        // output:  [(1, thingOne), (2, thingTwo), (3, thingThree)]
   }
}

