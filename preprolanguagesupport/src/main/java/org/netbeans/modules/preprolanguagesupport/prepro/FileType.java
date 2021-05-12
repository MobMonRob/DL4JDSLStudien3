package org.netbeans.modules.preprolanguagesupport.prepro;

import org.netbeans.api.annotations.common.StaticResource;

/**
 * Defines mime Type and Icon of PrePro
 * @author paula
 */
public interface FileType {
    @StaticResource
    String ICON = "org/netbeans/modules/preprolanguagesupport/prepro/PreProIcon.png";
    
    String MIME = "application/x-prepro";
}
