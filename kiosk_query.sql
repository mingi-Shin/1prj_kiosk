
----------------------------------------------------양준수--------------------------------------

DROP SEQUENCE SEQ_MEMBER_LEVEL_ID;
create sequence SEQ_MEMBER_LEVEL_ID -- MEMBERS 테이블에 level_id 컬럼의 값을 순차적으로 1씩 늘려주기 위한 시퀀스
increment by 1
start with 1
maxValue 99999999999
cache 30
nocycle;

--테이블의 관계때문에 역순으로 드랍
drop table EXTRA_INGREDIENT;
drop table table36;
drop table stock;
drop table menu;
drop table category;
DROP TABLE ORDERS;
DROP TABLE GUEST;
DROP TABLE MEMBERS;
DROP TABLE MEMBER_LEVEL;

CREATE TABLE MEMBER_LEVEL ( -- 등급 테이블
   level_id NUMBER DEFAULT SEQ_MEMBER_LEVEL_ID.NEXTVAL CONSTRAINT pk_level_level_id PRIMARY KEY,
    level_name VARCHAR2(30) DEFAULT '브론즈',
    accumulation_rate NUMBER,
    accumulated_amount NUMBER
    --1번 트리거 사용시 : CONSTRAINT fk_member FOREIGN KEY (member_id) REFERENCES MEMBERS(member_id) 추가
);
-- 등급 테이블 데이터
INSERT INTO member_level (level_name, accumulation_rate, accumulated_amount)
VALUES ('지렁이', 1, 0);

INSERT INTO member_level (level_name, accumulation_rate, accumulated_amount)
VALUES ('뱀', 5, 30000);

INSERT INTO member_level (level_name, accumulation_rate, accumulated_amount)
VALUES ('이무기', 8, 50000);

INSERT INTO member_level (level_name, accumulation_rate, accumulated_amount)
VALUES ('쌍용', 10, 100000);

SELECT * FROM MEMBER_LEVEL;
-- 1번 트리거
--CREATE OR REPLACE TRIGGER TRG_UPDATE_MEMBER_LEVEL
--AFTER INSERT ON ORDERS  -- 주문 테이블에 INSERT 후 실행
--FOR EACH ROW
--DECLARE
    --v_member_id NUMBER;
    --v_order_amount NUMBER;
    --v_current_amount NUMBER;
--BEGIN
    -- 1. 주문한 회원 ID와 금액 조회 (예시)
    --v_member_id := :NEW.member_id;
    --v_order_amount := :NEW.order_amount;

    -- 2. 기존 누적 금액 조회
    --SELECT accumulated_amount INTO v_current_amount
    --FROM MEMBER_LEVEL
    --WHERE member_id = v_member_id;  -- 주의: 현재 테이블에 member_id 컬럼이 없어서 수정 필요

    -- 3. 누적 금액 업데이트
    --UPDATE MEMBER_LEVEL
    --SET
        --accumulated_amount = v_current_amount + v_order_amount,
        --level_name = CASE
                       --WHEN v_current_amount + v_order_amount >= 5000000 THEN '플래티넘'
                        --WHEN v_current_amount + v_order_amount >= 1000000 THEN '골드'
                        --WHEN v_current_amount + v_order_amount >= 100000 THEN '실버'
                     --ELSE '브론즈'
                     --END
    --WHERE member_id = v_member_id;
--END;
--/

DROP SEQUENCE SEQ_MEMBERS_ID;
create sequence SEQ_MEMBERS_ID -- 회원 : 홀수(회원과 비회원을 구분하기위해 홀수 짝수로 나눔)
increment by 2
start with 1
maxValue 99999999999
cache 30
nocycle;


CREATE TABLE MEMBERS ( -- 회원 테이블
    member_id NUMBER DEFAULT SEQ_MEMBERS_ID.NEXTVAL CONSTRAINT pk_members_member_id PRIMARY KEY,
    phone_number VARCHAR2(30),
    total_amount NUMBER,
    points NUMBER,
    stamps NUMBER,
    level_id NUMBER CONSTRAINT fk_member_level_id REFERENCES MEMBER_LEVEL(level_id) ON DELETE CASCADE
);

INSERT INTO members (phone_number, total_amount, points, stamps, level_id)
VALUES ('010-1111-1111', 25000, 5000, 2, 1);

INSERT INTO members (phone_number, total_amount, points, stamps, level_id)
VALUES ('010-2222-2222', 30000, 3000, 3, 2);

INSERT INTO members (phone_number, total_amount, points, stamps, level_id)
VALUES ('010-3333-3333', 400000, 8000, 5, 4);

INSERT INTO members (phone_number, total_amount, points, stamps, level_id)
VALUES ('010-4444-4444', 100000, 2000, 8, 4);

INSERT INTO members (phone_number, total_amount, points, stamps, level_id)
VALUES ('010-5555-5555', 65000, 6400, 2, 3);

SELECT * FROM MEMBERS;

DROP SEQUENCE SEQ_GUEST_GUEST_ID;
create sequence SEQ_GUEST_GUEST_ID -- 비회원 : 홀수(회원과 비회원을 구분하기 위해 홀수 짝수로 나눔)
increment by 2
start with 2
maxValue 99999999999
cache 30
nocycle;


CREATE TABLE GUEST(--비회원 테이블
	guest_id NUMBER DEFAULT seq_guest_guest_id.NEXTVAL CONSTRAINT pk_guest_guest_id PRIMARY KEY
);
INSERT INTO guest values (DEFAULT);
INSERT INTO guest values (DEFAULT);
INSERT INTO guest values (DEFAULT);
SELECT * FROM GUEST;
DROP SEQUENCE SEQ_ORDERS_ORDER__ID;
create sequence SEQ_ORDERS_ORDER__ID -- orders 테이블에 order_id 컬럼의 값을 순차적으로 1씩 늘려주기 위한 시퀀스
increment by 1
start with 1
maxValue 999
cache 30
cycle;


CREATE TABLE ORDERS( -- 주문관리 테이블
	order_id NUMBER DEFAULT SEQ_ORDERS_ORDER__ID.NEXTVAL CONSTRAINT pk_orders_order_id PRIMARY KEY,
	member_id	NUMBER CONSTRAINT fk_orders_member_id REFERENCES MEMBERS(member_id) ON DELETE CASCADE,
	order_datetime DATE,
	price NUMBER,
	order_type VARCHAR2(30),
	order_status VARCHAR2(30),
	order_waiting_number NUMBER,
	guest_id NUMBER CONSTRAINT fk_orders_guest_id REFERENCES GUEST(guest_id) ON DELETE CASCADE,
	 CONSTRAINT chk_member_guest CHECK
        ((member_id IS NULL AND guest_id IS NOT NULL) OR
         (member_id IS NOT NULL AND guest_id IS NULL))
);


-- 3월 29일 주문 데이터 (조리완료 상태)
INSERT INTO orders (member_id, order_datetime, price, order_type, order_status, order_waiting_number, guest_id)
VALUES
(1, TO_DATE('2025-03-29 10:05:00', 'YYYY-MM-DD HH24:MI:SS'), 15000, '포장', '조리완료', 1, NULL);

INSERT INTO orders (member_id, order_datetime, price, order_type, order_status, order_waiting_number, guest_id)
VALUES
(3, TO_DATE('2025-03-29 10:15:00', 'YYYY-MM-DD HH24:MI:SS'), 18000, '포장', '조리완료', 3, NULL);

INSERT INTO orders (member_id, order_datetime, price, order_type, order_status, order_waiting_number, guest_id)
VALUES
(5, TO_DATE('2025-03-29 10:25:00', 'YYYY-MM-DD HH24:MI:SS'), 20000, '포장', '조리완료', 5, NULL);
-- 3월 30일 주문 데이터 (조리완료 상태)
INSERT INTO orders (member_id, order_datetime, price, order_type, order_status, order_waiting_number, guest_id)
VALUES
(NULL, TO_DATE('2025-03-30 09:30:00', 'YYYY-MM-DD HH24:MI:SS'), 11000, '홀', '조리완료', 2, 2);

SELECT * fROM ORDERS;
--2번 트리거
--CREATE OR REPLACE TRIGGER TRG_UPDATE_MEMBER_LEVEL
--AFTER INSERT OR UPDATE OF price, order_status ON ORDERS
--FOR EACH ROW
--DECLARE
    --v_total_amount NUMBER;  -- 회원의 총 누적 금액을 저장할 변수
    --v_level_id NUMBER;      -- 결정된 등급의 ID를 저장할 변수
    --v_is_member_order BOOLEAN := FALSE;  -- 회원 주문 여부 플래그
--BEGIN
    /*
     * 주문 상태가 '완료'가 아닌 경우 트리거 처리를 중단합니다.
     * 이는 주문이 완료된 경우에만 등급 업데이트를 수행하기 위함입니다.
     */
    --IF :NEW.order_status != '완료' THEN
        --RETURN;
    --END IF;

    /*
     * 회원 주문인지 확인합니다.
     * member_id가 NULL이 아니고 guest_id가 NULL인 경우에만 회원 주문으로 간주합니다.
     */
    --IF :NEW.member_id IS NOT NULL AND :NEW.guest_id IS NULL THEN
        --v_is_member_order := TRUE;
    --END IF;

    /* 회원 주문인 경우에만 등급 업데이트 로직을 수행합니다. */
    --IF v_is_member_order THEN
        /* 1. 회원의 총 누적 금액을 업데이트합니다. (현재 주문 금액을 추가) */
        --UPDATE MEMBERS
        --SET total_amount = total_amount + :NEW.price
        --WHERE member_id = :NEW.member_id;

        /* 2. 업데이트된 총 누적 금액을 조회합니다. */
        --SELECT total_amount INTO v_total_amount
        --FROM MEMBERS
        --WHERE member_id = :NEW.member_id;

        /*
         * 3. 누적 금액에 따라 적절한 등급을 결정합니다.
         * - 5,000,000원 이상: 플래티넘
         * - 1,000,000원 이상: 골드
         * - 100,000원 이상: 실버
         * - 그 미만: 브론즈
         */
        --IF v_total_amount >= 5000000 THEN
            --SELECT level_id INTO v_level_id FROM MEM


--------------------------------------------------------이호빈----------------------------------------------------


create table category ( --카테고리 테이블
category_id number constraint pk_category_category_id primary key,
menu varchar2(30)
);
INSERT INTO category (category_id, menu) VALUES (1, '세트');
INSERT INTO category (category_id, menu) VALUES (2, '버거용재료');
INSERT INTO category (category_id, menu) VALUES (3, '사이드메뉴');
INSERT INTO category (category_id, menu) VALUES (4, '음료');
INSERT INTO category (category_id, menu) VALUES (5, '추가재료');

SELECT * FROM CATEGORY;

drop sequence seq_menu_id;

create sequence seq_menu_id --메뉴 테이블에 id 컬럼의 값을 순차적으로 1씩 늘려주기 위한 시퀀스
start with 1
increment by 1
MAXVALUE 999
cache 30
nocycle;



create table menu (
    menu_id number default seq_menu_id.nextval CONSTRAINT pk_menu PRIMARY KEY,
    category_id number CONSTRAINT fk_order_category_category_id REFERENCES CATEGORY(category_id) ON DELETE CASCADE,
    menu_name varchar2(33),
    unit_name varchar2(30),
    image blob,
    weight number,
    calorie number,
    price number,
    notes clob,
    input_date date default sysdate
);

-- 버거
INSERT INTO menu (category_id, menu_name, unit_name, image, weight, calorie, price, notes, input_date) VALUES(2, 'HTML버거', 'g', EMPTY_BLOB(), 223, 582, 5500, TO_CLOB('100% 순 쇠고기 패티 두 장에 특별한 소스.'), SYSDATE);
INSERT INTO menu (category_id, menu_name, unit_name, image, weight, calorie, price, notes, input_date) VALUES(2, '오라클버거', 'g', EMPTY_BLOB(), 210, 603, 5900, TO_CLOB('100% 통닭다리살 겉바속촉 케이준 치킨 패티! 치킨 버거 본연의 맛에 충실한 클래식 버거'), SYSDATE);
INSERT INTO menu (category_id, menu_name, unit_name, image, weight, calorie, price, notes, input_date) VALUES(2, '쌍용버거', 'g', EMPTY_BLOB(), 237, 635, 4500, TO_CLOB('진한 불고기 소스의 패티가 두 장! 여기에 고소한 치즈와 마요네즈, 신선한 양상추를 곁들인 깊고 풍부한 맛.'), SYSDATE);
INSERT INTO menu (category_id, menu_name, unit_name, image, weight, calorie, price, notes, input_date) VALUES(2, '자바버거', 'g', EMPTY_BLOB(), 224, 409, 4700, TO_CLOB('탱~글한 통새우살 가득~!'), SYSDATE);
INSERT INTO menu (category_id, menu_name, unit_name, image, weight, calorie, price, notes, input_date) VALUES(2, 'C버거', 'g', EMPTY_BLOB(), 115, 318, 3000, TO_CLOB('고소하고 부드러운 치즈와 100% 순 쇠고기 패티, 심플한 클래식 치즈버거.'), SYSDATE);
INSERT INTO menu (category_id, menu_name, unit_name, image, weight, calorie, price, notes, input_date) VALUES(2, 'C++버거', 'g', EMPTY_BLOB(), 167, 479, 4800, TO_CLOB('고소하고 부드러운 치즈와 100% 순 쇠고기 패티가 두개'), SYSDATE);
INSERT INTO menu (category_id, menu_name, unit_name, image, weight, calorie, price, notes, input_date) VALUES(2, 'C#버거', 'g', EMPTY_BLOB(), 216, 640, 5900, TO_CLOB('부드러운 치즈와 풍부한 육즙의 패티를 세배 더 진하게 즐길 수 있는 트리플 치즈버거'), SYSDATE);
INSERT INTO menu (category_id, menu_name, unit_name, image, weight, calorie, price, notes, input_date) VALUES(2, '로우레벨버거', 'g', EMPTY_BLOB(), 101, 266, 2600, TO_CLOB('가장 기본, 근본 버거'), SYSDATE);

-- 세트
INSERT INTO menu (category_id, menu_name, unit_name, image, weight, calorie, price, notes, input_date) VALUES(1, 'HTML 세트', 'g', EMPTY_BLOB(), 523, 1045, 7400, NULL, SYSDATE);
INSERT INTO menu (category_id, menu_name, unit_name, image, weight, calorie, price, notes, input_date) VALUES(1, '오라클버거세트', 'g', EMPTY_BLOB(), 510, 1066, 7400, NULL, SYSDATE);
INSERT INTO menu (category_id, menu_name, unit_name, image, weight, calorie, price, notes, input_date) VALUES(1, '쌍용버거세트', 'g', EMPTY_BLOB(), 537, 1098, 6400, NULL, SYSDATE);
INSERT INTO menu (category_id, menu_name, unit_name, image, weight, calorie, price, notes, input_date) VALUES(1, '자바버거세트', 'g', EMPTY_BLOB(), 524, 872, 6000, NULL, SYSDATE);
INSERT INTO menu (category_id, menu_name, unit_name, image, weight, calorie, price, notes, input_date) VALUES(1, 'C버거세트', 'g', EMPTY_BLOB(), 415, 781, 5000, NULL, SYSDATE);
INSERT INTO menu (category_id, menu_name, unit_name, image, weight, calorie, price, notes, input_date) VALUES(1, 'C++버거세트', 'g', EMPTY_BLOB(), 467, 942, 6000, NULL, SYSDATE);
INSERT INTO menu (category_id, menu_name, unit_name, image, weight, calorie, price, notes, input_date) VALUES(1, 'C#버거 세트', 'g', EMPTY_BLOB(), 516, 1136, 7200, NULL, SYSDATE);

-- 사이드메뉴
INSERT INTO menu (category_id, menu_name, unit_name, image, weight, calorie, price, notes, input_date) VALUES(3, '코울슬로', 'g', EMPTY_BLOB(), 100, 179, 1900, TO_CLOB('양배추, 당근, 양파가 상큼하고 크리미한 마요 드레싱과 어우러져 아삭하게 씹히는 샐러드'), SYSDATE);
INSERT INTO menu (category_id, menu_name, unit_name, image, weight, calorie, price, notes, input_date) VALUES(3, '모짜렐라 치즈스틱 2조각', 'g', EMPTY_BLOB(), 47, 165, 2800, TO_CLOB('속이 꽉 찬 황금빛 바삭함! 자연 모짜렐라 치즈로 빈틈 없이 고소한 치즈스틱'), SYSDATE);
INSERT INTO menu (category_id, menu_name, unit_name, image, weight, calorie, price, notes, input_date) VALUES(3, '후렌치 후라이(M)', 'g', EMPTY_BLOB(), 114, 324, 2500, NULL, SYSDATE);
INSERT INTO menu (category_id, menu_name, unit_name, image, weight, calorie, price, notes, input_date) VALUES(3, '후렌치 후라이(L)', 'g', EMPTY_BLOB(), 140, 397, 3200, NULL, SYSDATE);
INSERT INTO menu (category_id, menu_name, unit_name, image, weight, calorie, price, notes, input_date) VALUES(3, '맥너겟 4', 'g', EMPTY_BLOB(), 64, 163, 2600, NULL, SYSDATE);
INSERT INTO menu (category_id, menu_name, unit_name, image, weight, calorie, price, notes, input_date) VALUES(3, '치킨텐더 2', 'g', EMPTY_BLOB(), 79, 197, 2700, NULL, SYSDATE);

-- 음료
INSERT INTO menu (category_id, menu_name, unit_name, image, weight, calorie, price, notes, input_date) VALUES(4, '망고피치아이스티(M)', 'ml', EMPTY_BLOB(), 400, 216, 3400, NULL, SYSDATE);
INSERT INTO menu (category_id, menu_name, unit_name, image, weight, calorie, price, notes, input_date) VALUES(4, '망고피치아이스티(L)', 'ml', EMPTY_BLOB(), 600, 342, 3900, NULL, SYSDATE);
INSERT INTO menu (category_id, menu_name, unit_name, image, weight, calorie, price, notes, input_date) VALUES(4, '콜라(M)', 'ml', EMPTY_BLOB(), 425, 133, 1900, NULL, SYSDATE);
INSERT INTO menu (category_id, menu_name, unit_name, image, weight, calorie, price, notes, input_date) VALUES(4, '콜라(L)', 'ml', EMPTY_BLOB(), 610, 185, 2400, NULL, SYSDATE);
INSERT INTO menu (category_id, menu_name, unit_name, image, weight, calorie, price, notes, input_date) VALUES(4, '콜라제로(M)', 'ml', EMPTY_BLOB(), 425, 0, 1900, NULL, SYSDATE);
INSERT INTO menu (category_id, menu_name, unit_name, image, weight, calorie, price, notes, input_date) VALUES(4, '콜라제로(L)', 'ml', EMPTY_BLOB(), 610, 0, 2400, NULL, SYSDATE);
INSERT INTO menu (category_id, menu_name, unit_name, image, weight, calorie, price, notes, input_date) VALUES(4, '스프라이트(M)', 'ml', EMPTY_BLOB(), 425, 140, 1900, NULL, SYSDATE);
INSERT INTO menu (category_id, menu_name, unit_name, image, weight, calorie, price, notes, input_date) VALUES(4, '스프라이트(L)', 'ml', EMPTY_BLOB(), 610, 194, 2400, NULL, SYSDATE);
INSERT INTO menu (category_id, menu_name, unit_name, image, weight, calorie, price, notes, input_date) VALUES(4, '스프라이트제로(M)', 'ml', EMPTY_BLOB(), 425, 0, 1900, NULL, SYSDATE);
INSERT INTO menu (category_id, menu_name, unit_name, image, weight, calorie, price, notes, input_date) VALUES(4, '스프라이트제로(L)', 'ml', EMPTY_BLOB(), 610, 0, 2400, NULL, SYSDATE);
INSERT INTO menu (category_id, menu_name, unit_name, image, weight, calorie, price, notes, input_date) VALUES(4, '환타(M)', 'ml', EMPTY_BLOB(), 425, 62, 1900, NULL, SYSDATE);
INSERT INTO menu (category_id, menu_name, unit_name, image, weight, calorie, price, notes, input_date) VALUES(4, '환타(L)', 'ml', EMPTY_BLOB(), 610, 86, 2400, NULL, SYSDATE);

SELECT * fROM MENU;

create table stock(  --재고 테이블
	menu_id number constraint fk_stock_menu_id references menu(menu_id) on  delete cascade,
	category_id number constraint fk_stock_category_id references category(category_id) on  delete cascade,
	quantity number
);
/* 부모 테이블에 값이 안들어가서 오류뜸 */
-- 재고 테이블 데이터
INSERT INTO stock (menu_id, category_id, quantity)
VALUES (1, 1, 100);

INSERT INTO stock (menu_id, category_id, quantity)
VALUES (2, 2, 100);

INSERT INTO stock (menu_id, category_id, quantity)
VALUES (3, 3, 100);

SELECT * FROM STOCK;




create table TABLE36 (  --입고관리 테이블
received_date date default sysdate,
quantity_received number,
menu_id number constraint fk_table36_menu_id  references menu (menu_id) on  delete cascade,
category_id number constraint fk_table36_category_id references category (category_id) on  delete cascade
);

--입고관리 테이블 데이터
INSERT INTO TABLE36 (received_date, quantity_received, menu_id, category_id)
VALUES (SYSDATE, 120, 1, 2);

INSERT INTO TABLE36 (received_date, quantity_received, menu_id, category_id)
VALUES (TO_DATE('2025-03-29 10:15:00', 'YYYY-MM-DD HH24:MI:SS'), 40, 2, 3);

INSERT INTO TABLE36 (received_date, quantity_received, menu_id, category_id)
VALUES (SYSDATE, 200, 3, 5);

SELECT * fROM TABLE36;


drop table admin;

create table admin( --관리자  테이블
id varchar2(30) constraint pk_admin primary key,
pass varchar2(30)
);
--관리자 계정 데이터
insert into admin(id, pass)
values ( 'kiosk', '1234');

SELECT * fROM ADMIN;

CREATE TABLE EXTRA_INGREDIENT( --추가재료 테이블
    order_id number CONSTRAINT fk_extra_ingredient_order_id REFERENCES ORDERS(order_id) ON DELETE CASCADE,
    menu_id number CONSTRAINT fk_extra_ingredient_menu_id REFERENCES MENU(menu_id) ON DELETE CASCADE,
    category_id number,
    quantity number

);
-- 추가재료 주문 테이블 데이터
-- 추가재료 1: 회원 주문에서 '로우레벨버거' 메뉴에 추가된 '치즈' 추가재료
-- 추가재료 부모키 문제
INSERT INTO EXTRA_INGREDIENT (order_id, menu_id, category_id, quantity)
VALUES (1, 1, 2, 1);  -- 회원 주문에서 로우레벨버거에 치즈 추가

-- 추가재료 2: 비회원 주문에서 '코울슬로' 메뉴에 '치킨텐더' 추가

INSERT INTO EXTRA_INGREDIENT (order_id, menu_id, category_id, quantity)
VALUES (2, 5, 3, 2);  -- 비회원 주문에서 코울슬로에 치킨텐더 2개 추가

SELECT* FROM EXTRA_INGREDIENT;

SELECT * FROM MEMBER_LEVEL;
SELECT * FROM MEMBERS;
SELECT * FROM GUEST;
SELECT * fROM ORDERS;
SELECT * FROM CATEGORY;
SELECT * fROM MENU;
SELECT * FROM STOCK;
SELECT * fROM TABLE36;
SELECT * fROM ADMIN;
SELECT* FROM EXTRA_INGREDIENT;
