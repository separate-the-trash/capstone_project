package com.example.demo.Service;

import com.example.demo.dto.PointDto;
import com.example.demo.dto.RewardDto;
import com.example.demo.model.User;
import com.example.demo.repository.PointRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Transactional
@Service
public class PointService {

    private final PointRepository pointRepository;

    public PointService(PointRepository pointRepository) {
        this.pointRepository=pointRepository;
    }

    public PointDto addPoint(PointDto pointDto) {
        User user = pointRepository.findById(pointDto.getId())
                .orElseThrow(() -> new IllegalArgumentException("없는 reward id 입니다."));

        user.add50Point();

        User savedReward = pointRepository.save(user);

        return new PointDto(savedReward.getId(), savedReward.getPoint(), "포인트가 적립되었습니다");
    }

    public PointDto usePoint(long id, RewardDto rewardDto) {
        User user = pointRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("없는 reward id 입니다."));

        if (user.getPoint() < rewardDto.getReward()) {
            throw new IllegalArgumentException("point 부족");
        }

        user.usePoint(rewardDto.getReward());

        User savedReward = pointRepository.save(user);

        return new PointDto(savedReward.getId(), savedReward.getPoint(), "구매가 완료되었습니다");
    }
}