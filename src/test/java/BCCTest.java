import net.md_5.bungee.chat.ComponentSerializer;
import org.junit.Test;

public class BCCTest {
    @Test
    public void testBC() {
        String json = "{\"text\":\"a\",\"hoverEvent\":{\"action\":\"show_item\",\"value\":[{\"text\":\"{id:\\\"minecraft:netherrack\\\",Count:47b}\"}]}}";
        System.out.println(ComponentSerializer.toString(ComponentSerializer.parse(json)[0]));
    }
}
