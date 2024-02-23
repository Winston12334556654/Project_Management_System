package ai.group2.project_management_system.service.Impl;

import ai.group2.project_management_system.model.entity.Position;
import ai.group2.project_management_system.repository.PositionRepository;
import ai.group2.project_management_system.service.PositionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PositionServiceImpl implements PositionService {

    private final PositionRepository positionRepository;



    @Override
    public List<Position> getAllPositions(){
        return positionRepository.findAll();
    }
}
