CREATE OR REPLACE PROCEDURE SSG.SP_COMM_INV
(
    P_SALESTR_NO          IN  VARCHAR2
  , P_SKU_CODE            IN  VARCHAR2
  , P_TYPE_CODE           IN  VARCHAR2
  , P_QUANTITY            IN  VARCHAR2
  , O_ERR_NO              OUT VARCHAR2
  , O_ERR_MSG             OUT VARCHAR2
)
/********************************************************************************
 *  1.Header
 *   작  성  자 : baeukchoi (baeuk2004@naver.com)
 *   최종수정자 : baeukchoi (baeuk2004@naver.com)
 *
 *  2. Description
 *
 *      일변동 재고 가감을 위한 공통 프로시져
 *
 *  3. Arguments
 *      P_SALESTR_NO          영업점번호
 *      P_SKU_CODE            SKU코드(상품ID)
 *      P_TYPE_CODE           재고반영유형(01:매입, 02:판매, 03:반품, 04:대출, 05:대입, 06:이관...)
 *      P_QUANTITY            재고반영수량
 *ㄴ
 *  4.Revision History.
 *    2024.07.29 최배욱 재고차감 DB 반영
 --********************************************************************************/
 
IS
    V_CNT                       NUMBER;
    V_STD_DTS 					VARCHAR2(8):= TO_CHAR(SYSDATE, 'YYYYMMDD');

BEGIN

    IF P_SALESTR_NO IS NULL OR P_SKU_CODE IS NULL OR P_TYPE_CODE IS NULL OR P_QUANTITY IS NULL THEN
      O_ERR_NO  := '01';
      O_ERR_MSG := 'MANDATORY PARAMETER IS NULL';
      RETURN;
    END IF;

    O_ERR_NO  := '00';
    O_ERR_MSG := '정상 처리되었습니다.';

--============================================================================================================
-- 일변동재고 처리
--============================================================================================================
    /*
    P_TYPE_CODE
    --변동재고
    01 : 매입
    02 : 판매
    03 : 반품(고객)
    04 : 대출(점간재고이동:출고)
    05 : 대입(점간재고이동:입고)
    06 : 이관(협력사반품)
    */

	-- STEP1 해당 일자의 변동재고 데이터 확인 AND 없으면 INSERT
	SELECT  COUNT(*) 		INTO V_CNT
	FROM    DD_VRB_INV
	WHERE   1				= 1
	AND     INV_DT 			= V_STD_DTS
	AND		SALESTR_NO 		= P_SALESTR_NO
	AND     SKU_CODE 		= P_SKU_CODE
	;

	IF V_CNT = 0 THEN
	    INSERT INTO DD_VRB_INV (
			  INV_DT
			, SALESTR_NO
			, SKU_CODE
			, TD_INV_QTY 	-- 당일재고반영(총)수량
			, BUY_QTY 		-- 매입(01)수량
			, SELL_QTY		-- 판매(02)수량
			, RET_QTY		-- 반품(03)수량
			, TRSF_OUT_QTY	-- 대출(04)수량
			, TRSF_IN_QTY   -- 대입(05)수량
			, RETTRSF_QTY	-- 이관(06)수량
			, REGPE_ID
			, REG_DTS
			, MODPE_ID
			, MOD_DTS
			, INV_YM		-- 재고연월
			)
	    VALUES (
		      V_STD_DTS
		    , P_SALESTR_NO
		    , P_SKU_CODE
		    , 0
		    , 0, 0, 0, 0, 0, 0
		    , 'SP_COMM_INV'
		    , SYSDATE
		    , 'SP_COMM_INV'
		    , SYSDATE
		    , SUBSTR(V_STD_DTS, 1, 6)
		   )
		   ;
	END IF;

	-- STEP1.2.2 변동 재고 업데이트
	IF P_TYPE_CODE = '01' 		THEN  -- 매입
	    UPDATE  DD_VRB_INV
	    SET     TD_INV_QTY 		= TD_INV_QTY + P_QUANTITY                    /* 당일재고수량 증가 */
		  	  , BUY_QTY 		= BUY_QTY + P_QUANTITY                       /* 매입수량 증가 */
		  	  , MOD_DTS 		= SYSDATE
		  	  , MODPE_ID 		= 'SP_COMM_INV'
	    WHERE   1				= 1
	    AND     INV_DT 			= V_STD_DTS
	    AND     SALESTR_NO 		= P_SALESTR_NO
	    AND     SKU_CODE 		= P_SKU_CODE
	    ;
	ELSIF P_TYPE_CODE = '02' THEN -- 판매
		UPDATE  DD_VRB_INV
		SET     TD_INV_QTY 		= TD_INV_QTY - P_QUANTITY                    /* 당일재고수량 감소 */
		      , SELL_QTY 		= SELL_QTY + P_QUANTITY                      /* 판매수량 증가 */
		      , MOD_DTS 		= SYSDATE
		      , MODPE_ID 		= 'SP_COMM_INV'
		WHERE   1				= 1
	    AND     INV_DT 			= V_STD_DTS
	    AND     SALESTR_NO 		= P_SALESTR_NO
	    AND     SKU_CODE 		= P_SKU_CODE
		;
	ELSIF P_TYPE_CODE = '03' THEN -- 반품
		UPDATE  DD_VRB_INV
		SET     TD_INV_QTY 		= TD_INV_QTY + P_QUANTITY                    /* 당일재고수량 증가 */
		      , RET_QTY 		= RET_QTY + P_QUANTITY                       /* 반품수량 증가 */
		      , MOD_DTS 		= SYSDATE
		      , MODPE_ID 		= 'SP_COMM_INV'
		WHERE   1				= 1
	    AND     INV_DT 			= V_STD_DTS
	    AND     SALESTR_NO 		= P_SALESTR_NO
	    AND     SKU_CODE 		= P_SKU_CODE
		;
	ELSIF P_TYPE_CODE = '04' THEN -- 대출
		UPDATE  DD_VRB_INV
		SET     TD_INV_QTY 		= TD_INV_QTY - P_QUANTITY                    /* 당일재고수량 감소 */
		      , TRSF_OUT_QTY 	= TRSF_OUT_QTY + P_QUANTITY                  /* 대출수량 증가 */
		      , MOD_DTS 		= SYSDATE
		      , MODPE_ID 		= 'SP_COMM_INV'
		WHERE   1				= 1
	    AND     INV_DT 			= V_STD_DTS
	    AND     SALESTR_NO 		= P_SALESTR_NO
	    AND     SKU_CODE 		= P_SKU_CODE
		;
	ELSIF P_TYPE_CODE = '05' THEN -- 대입
		UPDATE  DD_VRB_INV
		SET     TD_INV_QTY 		= TD_INV_QTY + P_QUANTITY                    /* 당일재고수량 증가 */
		      , TRSF_IN_QTY 	= TRSF_IN_QTY + P_QUANTITY                   /* 대입수량 증가 */
		      , MOD_DTS 		= SYSDATE
		      , MODPE_ID 		= 'SP_COMM_INV'
		WHERE   1				= 1
	    AND     INV_DT 			= V_STD_DTS
	    AND     SALESTR_NO 		= P_SALESTR_NO
	    AND     SKU_CODE 		= P_SKU_CODE
		;
	ELSIF P_TYPE_CODE = '06' THEN -- 이관
		UPDATE  DD_VRB_INV
		SET     TD_INV_QTY 		= TD_INV_QTY - P_QUANTITY                    /* 당일재고수량 감소 */
		      , RETTRSF_QTY 	= RETTRSF_QTY + P_QUANTITY                   /* 이관수량 증가 */
		      , MOD_DTS 		= SYSDATE
		      , MODPE_ID 		= 'SP_COMM_INV'
		WHERE   1				= 1
	    AND     INV_DT 			= V_STD_DTS
	    AND     SALESTR_NO 		= P_SALESTR_NO
	    AND     SKU_CODE 		= P_SKU_CODE
		;
	ELSE
		O_ERR_NO  := 1;
		O_ERR_MSG := '지원하지 않는 재고반영유형입니다.';
		ROLLBACK;
		RETURN;
	END IF;

    RETURN;

EXCEPTION
    WHEN OTHERS THEN
        O_ERR_NO  := '06';
        O_ERR_MSG := '(ECMS)재고적재오류:' || SQLERRM;
        RETURN;
END;