# 🚀 Insider Careers QA Test Automation

> **End-to-end test automation for Insider's careers website using Gauge, Selenium WebDriver, and Java**

[![Java](https://img.shields.io/badge/Java-11-orange.svg)](https://openjdk.java.net/)
[![Selenium](https://img.shields.io/badge/Selenium-4.21.0-green.svg)](https://selenium.dev/)
[![Gauge](https://img.shields.io/badge/Gauge-0.7.10-blue.svg)](https://gauge.org/)
[![Maven](https://img.shields.io/badge/Maven-3.6+-red.svg)](https://maven.apache.org/)

## 🎯 Overview

Automated testing framework for Insider's careers website focusing on QA department job listings, filtering, and application processes. Built with modern testing practices and cross-browser support.

## 🏗️ Tech Stack

| Component | Technology | Version |
|-----------|------------|---------|
| **Test Framework** | Gauge | 0.7.10 |
| **WebDriver** | Selenium | 4.21.0 |
| **Language** | Java | 11 |
| **Build Tool** | Maven | 3.6+ |
| **Assertions** | AssertJ | 3.17.2 |
| **Driver Management** | WebDriverManager | 5.8.0 |

## 📁 Project Structure
src/test/java/
├── driver/DriverFactory.java         
├── org/example/StepImplementation.java 
├── pages/                             
│   ├── BasePage.java                 
│   ├── MainPage.java                 
│   ├── CareersPage.java              
│   ├── CareerQualityAssurancePage.java
│   ├── OpenPositionsPage.java        
│   ├── ViewJobPage.java              
│   └── ApplicationFormPage.java      
└── utils/                            
    ├── ElementUtils.java             
    ├── ScrollUtils.java             
    └── ConfigReader.java
    
## 🚀 Quick Start

### Prerequisites
- Java 11+
- Maven 3.6+
- Chrome/Firefox/Edge browser

### Installation & Execution

```bash
# Clone and setup
git clone https://github.com/yagizyldrm/yagiz_yildirim_case
cd insider-case

# Install dependencies
mvn clean install

# Run all tests
mvn test

# Run specific scenario
mvn gauge:execute -DspecName="example.spec" -Dscenario="Insider Case"
```

## 🧪 Test Scenarios

### **Main Flow**
1. **Homepage** → Navigate & validate UI elements                       
2. **Careers** → Hover menu, click Careers, verify navigation           
3. **Content Blocks** → Verify Team, Location, Life at Insider sections 
4. **QA Department** → Navigate to QA careers, click "See all jobs"      
5. **Job Filtering** → Filter by Istanbul, Turkey + Quality Assurance   
6. **Application** → Click "View Role", verify form navigation          

### **Main Flow With Cookies :cookie: Acception**
1. **Homepage** → Navigate & validate UI elements
2. **Cookies** → Accept Cookies                      
3. **Careers** → Hover menu, click Careers, verify navigation           
4. **Content Blocks** → Verify Team, Location, Life at Insider sections 
5. **QA Department** → Navigate to QA careers, click "See all jobs"      
6. **Job Filtering** → Filter by Istanbul, Turkey + Quality Assurance   
7. **Application** → Click "View Role", verify form navigation

### **Bonus Scenario: Form Input Automation :memo:**
1. **QA Department** → Navigate to QA careers, click "See all jobs"      
2. **Job Filtering** → Filter by Istanbul, Turkey + Quality Assurance   
3. **Application** → Click "View Role", verify form navigation
4. **Form Filling** → Complete application form with test data
   
✅✅This scenario covers additional functional checkpoints required to complete the end-to-end testing flow in the Insider Test Case.

## 🔧 Key Features

### **Robust Automation**
- ✅ **Optimized XPath selectors** for performance | Expecting True
- ✅ **Explicit waits** with WebDriverWait          
- ✅ **Stale element handling** for dynamic content
- ✅ **Human-like interactions** (smooth scrolling, hover actions)
- ✅ **Soft assertions** for multiple validations

### **Advanced Capabilities**
- 🔄 **Window/tab handling** for new pages
- 📝 **Form filling** with validation
- 🎯 **Element finding by scrolling**
- ⚡ **Retry mechanisms** for flaky elements
- 📊 **Comprehensive HTML reports**

## ⚙️ Configuration

### Browser & Timeouts
```properties
# env/default.properties
browser=chrome
implicit.wait=10
explicit.wait=15
```

### Supported Browsers
- Chrome (chromedriver.exe)
- Firefox (geckodriver.exe)  

## 📊 Reports

Test execution generates detailed HTML reports in `reports/html-report/`:
- Execution summary with pass/fail counts
- Step-by-step results with timestamps
- Screenshots on failure
- Performance metrics

## 🛠️ Development

### Code Quality
- **Page Object Model** implementation
- **Optimized XPath selectors** (no `//*` usage)
- **Comprehensive error handling**
- **Clean code principles**
- **Detailed JavaDoc comments**

### Running Tests
```bash
# All scenarios
mvn gauge:execute

# Specific scenario
mvn gauge:execute -Dscenario="Insider Bonus Case"

# Debug mode
mvn gauge:execute -Dgauge.log.level=debug
```

## 🐛 Troubleshooting

| Issue | Solution |
|-------|----------|
| **Driver not found** | Ensure WebDriver executables are in `drivers/` directory |
| **Timeout errors** | Increase wait times in `env/default.properties` |
| **Element not found** | Check XPath selectors and page loading state |
| **Stale element** | Framework handles automatically with retry logic |

## 📈 Performance

- **Optimized selectors** reduce element finding time
- **Smart waiting** prevents unnecessary delays
- **Parallel execution** support (configurable)
- **Memory efficient** WebDriver management

## 📄 License

MIT License - see [LICENSE](LICENSE) file for details.
