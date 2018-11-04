package jobs;

import org.quartz.JobDataMap;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;
import service.ScheduleService;

@Component
public class InvoiceJob extends QuartzJobBean {

    @Autowired
    ScheduleService scheduleService;

    @Override
    protected void executeInternal(org.quartz.JobExecutionContext jobExecutionContext) throws JobExecutionException {

        JobDataMap jobDataMap = jobExecutionContext.getMergedJobDataMap();
        Integer dayOfMonth = jobDataMap.getIntegerFromString("dayOfMonth");

        scheduleService.generateContractInvoice();
        //scheduleService.generateInvoicesByDate2(dayOfMonth);
    }
}
