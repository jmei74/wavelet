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

### 6.1 Primary Data Sources

| Source | Type | Data Provided |
|--------|------|---------------|
| Wind | Commercial API | Bond data, quotes, curves |
| CFETS | Official API | Interbank market data |
| ChinaMoneyNet | Web Scraping/API | Public market data |

### 6.2 Fallback Strategy
- If primary sources unavailable, use cached data with timestamp warning
- Implement retry mechanism with exponential backoff
- Alert administrators on prolonged data unavailability

## 7. Acceptance Criteria

### AC-1: Data Ingestion
- [ ] System connects to configured data sources successfully
- [ ] Data sync runs on schedule without manual intervention
- [ ] Invalid data records are logged and skipped
- [ ] Data freshness indicators show last update time

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
└── README.md
```

## 9. Development Phases

### Phase 1: Foundation (Current)
- Project setup with Spring Boot
- Database schema design
- Basic CRUD operations
- Configuration management

### Phase 2: Data Integration
- Data source connector implementations
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
    primary: wind
    fallback: cfets
  sync:
    schedule: "0 0 6,18 * * *"  # 6AM and 6PM daily
  cache:
    ttl-minutes: 30
```

---

**Document Version:** 1.0  
**Last Updated:** 2026-04-05
