package org.netbeans.modules.preprolanguagesupport.prepro;

import org.netbeans.api.annotations.common.StaticResource;

/**
 * Defines mime Type and Icon of Prepro
 * @author paula
 */
public interface FileType {
    @StaticResource
    String ICON = "org/netbeans/modules/preprolanguagesupport/prepro/PreProIcon.png";
    
    String MIME = "text/x-prepro";
}
