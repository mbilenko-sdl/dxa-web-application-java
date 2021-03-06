package com.sdl.webapp.common.api.xpm;

import com.sdl.webapp.common.api.localization.Localization;

public interface XpmRegionConfig {
    /**
     * <p>getXpmRegion.</p>
     *
     * @param regionName   a {@link java.lang.String} object.
     * @param localization a {@link com.sdl.webapp.common.api.localization.Localization} object.
     * @return a {@link com.sdl.webapp.common.api.xpm.XpmRegion} object.
     */
    XpmRegion getXpmRegion(String regionName, Localization localization);
}
