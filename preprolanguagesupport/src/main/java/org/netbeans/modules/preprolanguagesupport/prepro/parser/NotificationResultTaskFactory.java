package org.netbeans.modules.preprolanguagesupport.prepro.parser;

import java.util.Collection;
import org.netbeans.api.editor.mimelookup.MimeRegistration;
import org.netbeans.modules.parsing.api.Snapshot;
import org.netbeans.modules.parsing.spi.SchedulerTask;
import org.netbeans.modules.parsing.spi.TaskFactory;

import static java.util.Collections.singletonList;
import org.netbeans.modules.preprolanguagesupport.prepro.FileType;

/**
 * Register parser tasks for PrePro
 * @author Paula
 */

@MimeRegistration(mimeType = FileType.MIME, service = TaskFactory.class)
public class NotificationResultTaskFactory extends TaskFactory{

    @Override
    public Collection<? extends SchedulerTask> create(Snapshot snpsht) {
        return singletonList(new NotificationResultTask());
    }
    
}
