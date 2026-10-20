package practice.mayank.co_stream.service;


import com.mongodb.client.result.UpdateResult;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;
import practice.mayank.co_stream.dto.KeysetPage;
import practice.mayank.co_stream.entity.Membership;

import java.util.*;

@Component
@RequiredArgsConstructor
public class MongoTemplateService {


    private final MongoTemplate mongoTemplate;


    public boolean runUpdateCriteria(Criteria criteria, Update update, Class<?> documentClass) {
        UpdateResult result = mongoTemplate.updateFirst(new Query(criteria), update, documentClass);
        return result.getMatchedCount() == 1;
    }

    public <T> KeysetPage<T> findByIdCursor(
            String filterField,
            Object filterValue,
            String before,
            String after,
            int pageSize,
            Class<T> entityClass
    ) {
        boolean hasBefore = before != null && !before.isBlank();
        boolean hasAfter = after != null && !after.isBlank();

        if (hasBefore && hasAfter) {
            throw new IllegalArgumentException("Use either 'before' or 'after', not both");
        }

        Criteria criteria = Criteria.where(filterField).is(filterValue);

        if (hasBefore) {
            if (!ObjectId.isValid(before)) throw new IllegalArgumentException("Invalid 'before' cursor");
            criteria = criteria.and("id").lt(before);
        } else if (hasAfter) {
            if (!ObjectId.isValid(after)) throw new IllegalArgumentException("Invalid 'after' cursor");
            criteria = criteria.and("id").gt(after);
        }

        // 'after' reads oldest-first so the page starts right next to the cursor; everything else reads newest-first
        Sort.Direction direction = hasAfter ? Sort.Direction.ASC : Sort.Direction.DESC;

        Query query = Query.query(criteria)
                .with(Sort.by(direction, "id"))
                .limit(pageSize + 1);

        List<T> rows = mongoTemplate.find(query, entityClass);

        boolean hasMore = rows.size() > pageSize;
        List<T> items = new ArrayList<>(hasMore ? rows.subList(0, pageSize) : rows);

        if (hasAfter) {
            Collections.reverse(items);
        }
        return new KeysetPage<>(items, hasMore);
    }


    public Optional<Membership> findRandomMember(String roomId, String excludeUserId) {
        Criteria criteria = Criteria.where("roomId").is(roomId);
        if (excludeUserId != null) {
            criteria.and("userId").ne(excludeUserId);
        }

        Aggregation aggregation = Aggregation.newAggregation(
                Aggregation.match(criteria),
                Aggregation.sample(1)
        );

        return Optional.ofNullable(
                mongoTemplate.aggregate(aggregation, Membership.class, Membership.class)
                        .getUniqueMappedResult());
    }


}
