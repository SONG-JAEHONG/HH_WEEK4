
# 조회 성능 개선 보고서 

---

## 1. 성능 저하 기능 식별 및 분석

---

### 🔹 Case 1: SeatHoldingScheduler - 만료된 HOLDING 좌석 해제

```java
List<Seat> expiredSeats = seatRepository.findByStatusAndBeforeExpire(
    SeatStatus.HOLDING, LocalDateTime.now());
```

**예상 SQL:**
```sql
SELECT * FROM seat
WHERE status = 'HOLDING' AND expire_time < '2025-07-30 12:00:00';
```

| 항목 | 내용                               |
|------|----------------------------------|
| 문제점 | Full Table Scan                  |
| 데이터 규모 | 1,000,000건 중 1000건 해당 시 전체 스캔 발생 |

**해결:**
```sql
CREATE INDEX idx_status_expire ON seat (status, expire_time);
```

**실행계획 개선:**


- Before: `type = ALL`, `key = NULL`
- After: `type = range`, `key = idx_status_expire`, `rows = 1000`

---

### 🔹 Case 2: ConcertService - 공연 날짜별 좌석 조회

```java
concertRepository.findAvailableSeatsByConcertDateId(concertDateId, SeatStatus.AVAILABLE)
```

**예상 SQL:**
```sql
SELECT * FROM seat
WHERE concert_date_id = ? AND status = 'AVAILABLE';
```

| 항목 | 내용                                                                                |
|------|-----------------------------------------------------------------------------------|
| 문제점 | Full Table Scan (Concert - ConcertDate - Seat 단방향 연관관계 이므로 추후 데이터 양이 많아 질 확률이 높음) |


**해결:**
```sql
CREATE INDEX idx_concert_date_status ON seat (concert_date_id, status);
```

---

### 🔹 Case 3: SeatHoldingScheduler - 좌석 저장 루프

```java
for (Seat seat : expiredSeats) {
    seatRepository.save(seat);
}
```

| 항목 | 내용 |
|------|------|
| 문제점 | 1건씩 save → JPA dirty checking, flush 반복 발생 |
| 병목 위치 | row 수 많을수록 flush/write 성능 저하 |

**해결:**
- `saveAll(expiredSeats)` 사용
-  JPQL로 bulk update 후 `@Modifying(clearAutomatically = true, flushAutomatically = true)` 으로 영속성 관리(일치)
```java
@Modifying(clearAutomatically = true, flushAutomatically = true)
@Query("UPDATE Seat s SET s.status = 'AVAILABLE' WHERE s.status = 'HOLDING' AND s.expireTime < :now")
void releaseExpiredSeats(@Param("now") LocalDateTime now);
```

---

## 2. 기대 효과

- Full Scan 제거
- JPA 저장 성능 개선 → 트랜잭션 처리 효율 증가

---

## 3. 인덱스 설계 요약

| 인덱스 이름 | 컬럼 조합 | 목적               |
|-------------|-----------|------------------|
| `idx_status_expire` | (status, expire_time) | 스케줄러 만료좌석 조회     |
| `idx_concert_date_status` | (concert_date_id, status) | 날짜별 좌석 상태 조회     |




