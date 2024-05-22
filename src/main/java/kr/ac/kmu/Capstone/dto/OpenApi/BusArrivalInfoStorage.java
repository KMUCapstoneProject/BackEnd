package kr.ac.kmu.Capstone.dto.OpenApi;

import kr.ac.kmu.Capstone.OpenApi.VO.BusApiServiceItem;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class BusArrivalInfoStorage {
    private List<BusApiServiceItem> busArrivalInfoList = new ArrayList<>();

    public synchronized void setBusArrivalInfoList(List<BusApiServiceItem> busArrivalInfoList) {
        this.busArrivalInfoList = busArrivalInfoList;
    }

    public synchronized List<BusApiServiceItem> getBusArrivalInfoList() {
        return new ArrayList<>(busArrivalInfoList);
    }
}
