# Wavelet - China Fixed Income Market Analytics Platform

## 1. Project Overview

**Project Name:** Wavelet  
**Project Type:** Financial Data Analytics Web Application  
**Core Functionality:** A platform that fetches market data from China's fixed income market, processes the data using wavelet-based algorithms, generates calculation results (yields, durations, risk metrics), and presents the results through an internal web dashboard.  
**Target Users:** Internal analysts and traders at financial institutions

## 2. Glossary

- **Fixed Income Market:** Market for debt securities (bonds, notes) - in this context, China's interbank bond market and exchange-traded bonds
- **China Fixed Income Data Sources:** Wind, Bloomberg, ChinaMoneyNet, CFETS (China Foreign Exchange Trade System)
- **Wavelet Transform:** Mathematical technique for time-frequency analysis of time series data
- **Yield Curve:** Graph showing relationship between bond yields and maturities
- **Duration:** Measure of bond price sensitivity to interest rate changes
- **Convexity:** Second-order measure of bond price sensitivity
- **VaR:** Value at Risk - statistical measure of market risk

## 3. System Architecture

```
┌─────────────────┐     ┌─────────────────┐     ┌─────────────────┐
│  Data Sources   │────▶│  Data Processor │────▶│  Calculation    │
│  (Fixed Income) │     │  (Wavelet Trans)│     │  Engine         │
└─────────────────┘     └─────────────────┘     └─────────────────┘
                                                        │
                                                        ▼
┌─────────────────┐     ┌─────────────────┐     ┌─────────────────┐
│  Web Frontend   │◀────│  REST API       │◀────│  Results Store  │
│  (Dashboard)    │     │                 │     │                 │
└─────────────────┘     └─────────────────┘     └─────────────────┘
```

## 4. Module Specifications

### 4.1 Data Ingestion Module

**Purpose:** Fetch and ingest market data from China fixed income market sources

**Features:**
- Connect to data sources via REST APIs (Wind, CFETS, ChinaMoneyNet)
- Scheduled data sync (configurable intervals: daily, hourly, real-time)
- Data validation and error handling
- Store raw data in PostgreSQL database

**Data Entities:**
| Entity | Description |
|--------|-------------|
| Bond | Bond master data (code, name, issuer, maturity, coupon) |
| Quote | Daily price quotes (bid, ask, mid, volume) |
| CurvePoint | Yield curve data points by tenor |
| Trade | Historical trade records |

### 4.2 Data Processing Module

**Purpose:** Process raw market data using wavelet transforms and statistical methods

**Features:**
- Wavelet decomposition for noise reduction
- Time series smoothing and trend extraction
- Outlier detection and handling
- Data aggregation by issuer, rating, maturity bucket

**Processing Operations:**
- Denoise price series using Daubechies wavelet
- Extract trend components from yield curves
- Identify structural breaks in time series

### 4.3 Calculation Engine Module

**Purpose:** Generate financial metrics and risk measures

**Calculations:**
| Metric | Formula/Method |
|--------|----------------|
| Yield to Maturity | IRR solver for bond cash flows |
| Modified Duration | (Macaulay Duration) / (1 + y/n) |
| Convexity | Second derivative of price w.r.t. yield |
| DV01 | Dollar value of 1 basis point |
| Spread to Benchmark | Bond yield - Benchmark yield |
| Term Structure | Nelson-Siegel-Svensson model fitting |

### 4.4 Web Presentation Module

**Purpose:** Display calculation results on internal web dashboard

**Pages:**
| Page | Content |
|------|---------|
| Dashboard | Overview of market metrics, key yields, daily changes |
| Bond Search | Search and filter bonds, view detailed analytics |
| Yield Curve | Interactive yield curve visualization |
| Risk Report | Portfolio-level risk metrics (duration, VaR) |
| Data Monitor | Data sync status, last update times, error logs |

**UI Requirements:**
- Responsive design for desktop and tablet
- Data tables with sorting, filtering, pagination
- Charts using ECharts or Chart.js
- Export to CSV/Excel

## 5. Technical Stack

| Layer | Technology |
|-------|------------|
| Backend | Java 17, Spring Boot 3 |
| Database | PostgreSQL |
| ORM | Spring Data JPA |
| Web | Thymeleaf + Bootstrap |
| Charts | ECharts |
| Build | Maven |
| Scheduling | Spring Scheduler |

## 6. Data Source Integration

### 6.1 Primary Data Source: AKShare

AKShare (https://github.com/akfamily/akshare) is an open-source Python financial data library providing comprehensive China bond market data.

**Integration Architecture:**
```
┌─────────────────────────────────────────────────────────────────┐
│                      Wavelet Application (Java)                 │
│  ┌──────────────┐    ┌──────────────┐    ┌──────────────────┐  │
│  │ Data Service │───▶│ ProcessBridge │───▶│ Python AKShare   │  │
│  │   (Java)     │    │ (ProcessExec)│    │   Scripts        │  │
│  └──────────────┘    └──────────────┘    └──────────────────┘  │
│         │                                       │               │
│         ▼                                       ▼               │
│  ┌──────────────┐                      ┌──────────────────┐      │
│  │ PostgreSQL   │◀─────────────────────│ JSON Output     │      │
│  │   Database   │                      │ (Stdout)         │      │
│  └──────────────┘                      └──────────────────┘      │
└─────────────────────────────────────────────────────────────────┘
```

**Available AKShare Bond APIs:**

| Function | Description | Data |
|----------|-------------|------|
| `bond_spot_quote()` | Interbank market maker quotes | bid/ask price, yield |
| `bond_spot_deal()` | Spot market trade data | price, yield, volume |
| `bond_china_yield()` | ChinaBond yield curves | tenor vs yield |
| `bond_info_cm()` | Bond master data | bond details from CFETS |
| `bond_info_detail_cm()` | Bond detail info | full bond profile |
| `bond_debt_nafmii()` | Interbank debt registration | registration data |

**Python Data Fetcher Scripts:**

| Script | Function |
|--------|----------|
| `fetch_quotes.py` | Fetch interbank bond quotes |
| `fetch_trades.py` | Fetch spot market trades |
| `fetch_yield_curve.py` | Fetch yield curve data |
| `fetch_bond_info.py` | Fetch bond master data |

**Installation:**
```bash
pip install akshare pandas
```

### 6.2 Data Entities Mapping

| AKShare Data | Database Entity | Fields |
|--------------|----------------|--------|
| bond_spot_quote | Quote | bond_code, bid_price, ask_price, bid_yield, ask_yield, institution |
| bond_spot_deal | Trade | bond_code, price, yield, change_bp, volume, weighted_yield |
| bond_china_yield | CurvePoint | curve_name, date, tenor_3m, tenor_6m, tenor_1y, tenor_3y, tenor_5y, tenor_7y, tenor_10y, tenor_30y |
| bond_info_cm | Bond | bond_code, bond_name, issuer, bond_type, issue_date, maturity_date, coupon_rate, rating |

### 6.3 Fallback Strategy
- If AKShare unavailable, use cached data with timestamp warning
- Implement retry mechanism with exponential backoff (3 attempts, 2x delay)
- Alert administrators on prolonged data unavailability

### 6.4 Future Enhancement: Wind Integration
Wind (paid) can replace AKShare for production use:
- Wind Server API: https://www.wind.com.cn/mobile/WDS/sapi/en.html
- Requires Wind Terminal license and Python integration

## 7. Acceptance Criteria

### AC-1: Data Ingestion
- [ ] System connects to AKShare Python scripts successfully
- [ ] Data sync runs on schedule without manual intervention
- [ ] Invalid data records are logged and skipped
- [ ] Data freshness indicators show last update time
- [ ] Python process errors are captured and logged in Java

### AC-2: Data Processing
- [ ] Wavelet denoising reduces noise in price series
- [ ] Processing completes within reasonable time (< 5 minutes for daily batch)
- [ ] Processed data is stored separately from raw data

### AC-3: Calculations
- [ ] Yield to maturity calculation matches Bloomberg/Refinitiv values
- [ ] Duration and convexity calculations are accurate
- [ ] Calculations handle edge cases (zero coupon, perpetual bonds)

### AC-4: Web Interface
- [ ] Dashboard loads within 3 seconds
- [ ] Bond search returns results within 1 second
- [ ] Charts render correctly on all supported browsers
- [ ] Data can be exported to CSV format

### AC-5: System Operations
- [ ] Application starts without errors
- [ ] Database migrations run successfully
- [ ] Health check endpoint returns 200 OK
- [ ] Error logs provide actionable debugging information

## 8. Project Structure

```
wavelet/
├── pom.xml
├── src/
│   ├── main/
│   │   ├── java/com/mycompany/wavelet/
│   │   │   ├── WaveletApplication.java
│   │   │   ├── config/
│   │   │   ├── controller/
│   │   │   ├── service/
│   │   │   ├── repository/
│   │   │   ├── model/
│   │   │   │   ├── entity/
│   │   │   │   └── dto/
│   │   │   ├── processor/
│   │   │   ├── calculation/
│   │   │   └── datasource/
│   │   └── resources/
│   │       ├── application.yml
│   │       ├── templates/
│   │       └── static/
│   └── test/
│       └── java/com/mycompany/wavelet/
├── scripts/
│   ├── fetch_quotes.py
│   ├── fetch_trades.py
│   ├── fetch_yield_curve.py
│   └── fetch_bond_info.py
├── requirements.txt
└── README.md
```

## 9. Development Phases

### Phase 1: Foundation (Current)
- Project setup with Spring Boot
- Database schema design
- Basic CRUD operations
- Configuration management

### Phase 2: AKShare Integration
- Python environment setup
- AKShare script development (fetch_quotes.py, fetch_trades.py, etc.)
- Java ProcessBuilder integration
- JSON parsing layer
- Scheduled data sync jobs
- Data validation layer

### Phase 3: Processing & Calculation
- Wavelet transform implementation
- Financial calculation engine
- Result caching and optimization

### Phase 4: Web Interface
- Dashboard development
- Interactive charts
- Data export functionality

## 10. Configuration

### application.yml structure
```yaml
wavelet:
  datasource:
    akshare:
      enabled: true
      python-path: /usr/bin/python3
      script-dir: /opt/wavelet/scripts
  sync:
    schedule: "0 0 6,18 * * *"  # 6AM and 6PM daily
    quotes-cron: "0 30 6,18 * * *"
    trades-cron: "0 0 7,19 * * *"
    yield-curve-cron: "0 0 6 * * *"
  cache:
    ttl-minutes: 30
```

### requirements.txt
```
akshare>=1.14.0
pandas>=2.0.0
```

## 11. Sample Python Scripts

### scripts/fetch_quotes.py
```python
import json
import akshare as ak

def main():
    df = ak.bond_spot_quote()
    result = df.to_dict(orient='records')
    print(json.dumps(result, ensure_ascii=False))

if __name__ == "__main__":
    main()
```

### scripts/fetch_yield_curve.py
```python
import json
import akshare as ak
from datetime import datetime

def main():
    today = datetime.now().strftime("%Y%m%d")
    df = ak.bond_china_yield(start_date=today, end_date=today)
    result = df.to_dict(orient='records')
    print(json.dumps(result, ensure_ascii=False))

if __name__ == "__main__":
    main()
```

---

**Document Version:** 1.1  
**Last Updated:** 2026-04-05
