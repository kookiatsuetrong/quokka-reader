import java.math.BigDecimal;
import java.time.Instant;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity @Table(name="prices")
class Price {
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	int code;
	
	String symbol;
	
	BigDecimal buy;
	
	BigDecimal sell;
	
	Instant time;
}
