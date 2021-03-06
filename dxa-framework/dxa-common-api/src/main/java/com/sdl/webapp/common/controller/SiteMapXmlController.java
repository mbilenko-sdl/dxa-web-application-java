package com.sdl.webapp.common.controller;

import com.sdl.webapp.common.api.WebRequestContext;
import com.sdl.webapp.common.api.content.NavigationProvider;
import com.sdl.webapp.common.api.content.NavigationProviderException;
import com.sdl.webapp.common.api.model.entity.SitemapItem;
import com.sdl.webapp.common.markup.Markup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;

import static com.sdl.webapp.common.controller.ControllerUtils.SERVER_ERROR_VIEW;
import static com.sdl.webapp.common.controller.RequestAttributeNames.MARKUP;

/**
 * Controller which returns the sitemap in XML format.
 */
@Controller
public class SiteMapXmlController {
    private static final Logger LOG = LoggerFactory.getLogger(SiteMapXmlController.class);

    private final WebRequestContext webRequestContext;

    private final NavigationProvider navigationProvider;

    private final Markup markup;

    /**
     * <p>Constructor for SiteMapXmlController.</p>
     *
     * @param webRequestContext  a {@link com.sdl.webapp.common.api.WebRequestContext} object.
     * @param navigationProvider a {@link com.sdl.webapp.common.api.content.NavigationProvider} object.
     * @param markup             a {@link com.sdl.webapp.common.markup.Markup} object.
     */
    @Autowired
    public SiteMapXmlController(WebRequestContext webRequestContext, NavigationProvider navigationProvider,
                                Markup markup) {
        this.webRequestContext = webRequestContext;
        this.navigationProvider = navigationProvider;
        this.markup = markup;
    }

    private static void writeSitemapItemsXml(List<SitemapItem> items, PrintWriter out) {
        for (SitemapItem item : items) {
            if (item.getType().equals("Page") && item.getUrl().startsWith("/")) {
                out.println("<url>");
                out.println("<loc>" + item.getUrl() + "</loc>");
                if (item.getPublishedDate() != null) {
                    out.println("<lastmod>" + item.getPublishedDate() + "</lastmod>");
                }
                out.println("</url>");
            } else {
                writeSitemapItemsXml(item.getItems(), out);
            }
        }
    }

    /**
     * Handles a request for the sitemap in XML format.
     *
     * @return The sitemap in XML format.
     * @throws com.sdl.webapp.common.api.content.NavigationProviderException If an error occurs so that the navigation data cannot be retrieved.
     */
    @RequestMapping(method = RequestMethod.GET, value = "/sitemap.xml", produces = "application/xml; charset=utf-8")
    @ResponseBody
    public String handleGetSiteMapXml() throws NavigationProviderException {
        LOG.trace("handleGetSiteMapXml");

        final SitemapItem navigationModel = navigationProvider.getNavigationModel(webRequestContext.getLocalization());

        final StringWriter sw = new StringWriter();
        final PrintWriter out = new PrintWriter(sw);

        out.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        out.println("<urlset xmlns=\"http://www.sitemaps.org/schemas/sitemap/0.9\">");
        writeSitemapItemsXml(navigationModel.getItems(), out);
        out.println("</urlset>");

        return sw.toString();
    }

    /**
     * <p>handleException.</p>
     *
     * @param request   a {@link javax.servlet.http.HttpServletRequest} object.
     * @param exception a {@link java.lang.Exception} object.
     * @return a {@link java.lang.String} object.
     */
    @ExceptionHandler(Exception.class)
    public String handleException(HttpServletRequest request, Exception exception) {
        request.setAttribute(MARKUP, markup);
        return SERVER_ERROR_VIEW;
    }
}
