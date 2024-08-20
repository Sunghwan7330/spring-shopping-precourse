# spring-shopping-precourse

## 기능 목록
### 공통 기능
- [x] 상품 존재하는지 체크
- [x] 이미지 URL 체크

### 요구 사항 기능
- [x] 상품 목록 조회
- [x] 특정 상품 목록 조회
- [x] 이름, 가격, 이미지 URL을 입력받아 상품 추가 
- [x] 상품 수정
- [x] 상품 삭제

# 사용자 스토리

## 상품 이름은 공백을 포함하여 최대 15자까지 입력할 수 있음

```gherkin
Given 상품 이름이 "동해물과 백두산이 마르고 닳도록"
 When 상품을 생성하면
 Then 400 Bad Request를 반환한다.
  And "상품의 이름은 15자를 넘길 수 없습니다."라고 응답한다.
```

```gherkin
Given 기존 상품이 존재할 때 
  And 변경하는 상품의 이름이 "동해물과 백두산이 마르고 닳도록"
 When 상품을 수정하면
 Then 400 Bad Request를 반환한다.
  And "상품의 이름은 15자를 넘길 수 없습니다."라고 응답한다.
```

## 상품명은 일부 특수 문자를 사용할 수 있음

```gherkin
Given 상품명이 "()[]+-&/_" 일떄
 When 상품을 추가하면
 Then 200 Success 를 응답한다.
```

```gherkin
Given 상품명이 "$#@^\%" 일떄
 When 상품을 추가하면
 Then  400 Bad Request를 반환한다.
  And "상품의 특수 문자는 '( ), [ ], +, -, &, /, _' 만 포함할 수 있습니다."라고 응답한다.
```

```gherkin
Given 상품명이 "()[]+-&/_" 일떄
 When 상품을 수정하면
 Then 200 Success 를 응답한다.
```

```gherkin
Given 상품명이 "$#@^\%" 일떄
 When 상품을 수정하면
 Then  400 Bad Request를 반환한다.
  And "상품의 특수 문자는 '( ), [ ], +, -, &, /, _' 만 포함할 수 있습니다."라고 응답한다.
```

## 상품명은 비속어를 포함할 수 없음

```gherkin
Given 상품명이 "fuck" 일떄
 When 상품을 추가하면
 Then 400 Bad Request를 반환한다.
  And "상품명에는 욕설이 포함될 수 없습니다." 라고 응답한다.
```

```gherkin
Given 상품명이 "fuck" 일떄
 When 상품을 수정하면
 Then 400 Bad Request를 반환한다.
  And "상품명에는 욕설이 포함될 수 없습니다." 라고 응답한다.
```
