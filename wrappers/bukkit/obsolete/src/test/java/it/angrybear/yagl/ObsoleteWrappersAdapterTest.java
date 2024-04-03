package it.angrybear.yagl;

import it.angrybear.yagl.wrappers.Sound;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class ObsoleteWrappersAdapterTest {

    @Test
    void testPlaySound() {
        Sound sound = new Sound(org.bukkit.Sound.AMBIENCE_CAVE.name(),10, 2, "BLOCKS");
        Player player = mock(Player.class);

        when(player.getLocation()).thenReturn(new Location(null, 0, 1, 0));

        WrappersAdapter.playSound(player, sound);

        ArgumentCaptor<org.bukkit.Sound> soundArg = ArgumentCaptor.forClass(org.bukkit.Sound.class);
        ArgumentCaptor<Float> volumeArg = ArgumentCaptor.forClass(Float.class);
        ArgumentCaptor<Float> pitchArg = ArgumentCaptor.forClass(Float.class);
        verify(player).playSound(any(Location.class), soundArg.capture(), volumeArg.capture(), pitchArg.capture());

        assertEquals(sound.getName(), soundArg.getValue().name());

        assertEquals(sound.getVolume(), volumeArg.getValue());
        assertEquals(sound.getPitch(), pitchArg.getValue());
    }
}
