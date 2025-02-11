# 🏦 Banking App API  

A secure and scalable Banking API built with **Spring Boot**, **Spring Security**, and **OpenAPI**, featuring real-time currency updates, account management, transactions, and card services.

## 🚀 Features  
- ✅ **User Registration & Authentication** (Spring Security, JWT)  
- ✅ **Account Management** (Create, Transfer, Find Accounts)  
- ✅ **Card Management** (Create, Credit, Debit)  
- ✅ **Transaction History** (By Account/Card)  
- ✅ **Real-Time Currency Exchange Rates** (Integrated with External APIs)  
- ✅ **OpenAPI Documentation**  

## 📂 API Endpoints  

### 🔑 **Authentication**  
| Method | Endpoint | Description |
|--------|---------|------------|
| `POST` | `/user/register` | Register a new user |
| `POST` | `/user/auth` | Authenticate user and get JWT token |

### 💰 **Account Management**  
| Method | Endpoint | Description |
|--------|---------|------------|
| `GET` | `/accounts` | Fetch all user accounts |
| `POST` | `/accounts` | Create a new account |
| `POST` | `/accounts/transfer` | Transfer funds between accounts |
| `POST` | `/accounts/find` | Find an account by details |

### 🏦 **Card Management**  
| Method | Endpoint | Description |
|--------|---------|------------|
| `POST` | `/card/create` | Create a new debit/credit card |
| `POST` | `/card/debit` | Debit a card |
| `POST` | `/card/credit` | Credit a card |
| `GET` | `/card` | Get card details |

### 💱 **Currency Exchange (Real-Time Updates)**  
| Method | Endpoint | Description |
|--------|---------|------------|
| `GET` | `/accounts/rates` | Fetch live exchange rates |
| `POST` | `/accounts/convert` | Convert currency |

### 📜 **Transaction History**  
| Method | Endpoint | Description |
|--------|---------|------------|
| `GET` | `/transactions` | Fetch all transactions |
| `GET` | `/transactions/c/{cardId}` | Fetch transactions by Card ID |
| `GET` | `/transactions/a/{accountId}` | Fetch transactions by Account ID |

---

## 🛠️ **Tech Stack**
- **Spring Boot** (Backend Framework)  
- **Spring Security & JWT** (Authentication & Authorization)  
- **MySQL** (Database)  
- **OpenAPI** (API Documentation)  
- **RestTemplate / WebClient** (Currency Exchange API)  
- **Docker & Cloud Run** (Deployment)  

---

## 🔐 **Security - Spring Security & JWT**  
- Uses **JWT Token Authentication** for securing endpoints.  
- Role-based access control for **Admin & User Roles**.  
- Passwords encrypted using **BCrypt**.  

---

## ⚡ **Real-Time Currency Updates**  
This application fetches live exchange rates from an external API using `RestTemplate` or `WebClient` and updates rates dynamically.

```java
@RestController
@RequestMapping("/accounts/rates")
public class CurrencyExchangeController {

    private final RestTemplate restTemplate = new RestTemplate();

    @GetMapping
    public Map<String, Double> getExchangeRates() {
        String apiUrl = "https://api.exchangeratesapi.io/latest?base=USD";
        ResponseEntity<Map> response = restTemplate.getForEntity(apiUrl, Map.class);
        return (Map<String, Double>) response.getBody().get("rates");
    }
}
