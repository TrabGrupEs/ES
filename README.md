# Hotel Reservation System (HRS)

Sistema de reservas hoteleiras desenvolvido como projeto final da UC de Engenharia de Software no Instituto Politécnico de Santarém.

## 📋 Descrição

O Hotel Reservation System é uma aplicação Java completa que implementa a gestão operacional de um hotel, incluindo reservas, check-in/check-out, faturação, housekeeping, manutenção e relatórios financeiros.

## 🏗️ Arquitetura

O sistema segue uma **arquitetura em camadas** (Layered Architecture) com separação clara de responsabilidades:

- **domain**: Entidades do negócio (User, Room, Reservation, Invoice, etc.)
- **service**: Regras de negócio e lógica da aplicação
- **repository**: Camada de persistência (implementação em memória)
- **factory**: Padrão Factory para criação de utilizadores
- **observer**: Padrão Observer para notificações de mudanças de estado
- **security**: Autenticação e controlo de permissões
- **util**: Classes auxiliares (ReportGenerator)
- **app**: Classe principal de execução

## 🎨 Design Patterns Implementados

### 1. Factory Pattern
- **Classe**: `UserFactory`
- **Propósito**: Criação centralizada de diferentes tipos de utilizadores (Guest, Receptionist, Manager, Administrator)
- **Benefício**: Desacopla a criação de objetos do código cliente

### 2. Singleton Pattern
- **Classes**: `PricingService`, `SecurityService`
- **Propósito**: Garantir uma única instância de serviços críticos
- **Benefício**: Consistência de dados e regras em todo o sistema

### 3. Observer Pattern
- **Classes**: `RoomSubject`, `RoomObserver`, `HousekeepingObserver`, `ReceptionObserver`
- **Propósito**: Notificação automática quando o estado de um quarto muda
- **Benefício**: Desacoplamento entre módulos e atualizações automáticas

## ✅ Princípios SOLID

- **S** - Single Responsibility: Cada serviço tem uma responsabilidade única
- **O** - Open/Closed: Extensível através de interfaces e herança
- **L** - Liskov Substitution: Subclasses de User são substituíveis
- **I** - Interface Segregation: Interfaces específicas por repositório
- **D** - Dependency Inversion: Serviços dependem de abstrações (interfaces)

## 🔐 Sistema de Segurança

O sistema implementa controlo de acesso baseado em roles:

- **GUEST**: Consultar quartos, fazer reservas
- **RECEPTIONIST**: Gestão de reservas, check-in/check-out
- **MANAGER**: Todas as permissões + gestão de preços e relatórios
- **ADMIN**: Acesso total ao sistema

## 📦 Estrutura do Projeto

```
src/
├── app/
│   └── HotelReservationSystem.java (Main)
├── domain/
│   ├── enums/
│   │   ├── RoomStatus.java
│   │   ├── RoomType.java
│   │   ├── ReservationStatus.java
│   │   ├── UserRole.java
│   │   ├── PaymentMethod.java
│   │   └── MaintenanceStatus.java
│   ├── User.java (abstract)
│   ├── Guest.java
│   ├── Receptionist.java
│   ├── Manager.java
│   ├── Administrator.java
│   ├── Room.java
│   ├── Reservation.java
│   ├── Invoice.java
│   └── MaintenanceRequest.java
├── service/
│   ├── ReservationService.java
│   ├── RoomService.java
│   ├── CheckInService.java
│   ├── CheckOutService.java
│   ├── PricingService.java (Singleton)
│   ├── InvoiceService.java
│   ├── HousekeepingService.java
│   └── MaintenanceService.java
├── repository/
│   ├── RoomRepository.java (interface)
│   ├── ReservationRepository.java (interface)
│   ├── InMemoryRoomRepository.java
│   └── InMemoryReservationRepository.java
├── factory/
│   └── UserFactory.java
├── observer/
│   ├── RoomObserver.java (interface)
│   ├── RoomSubject.java
│   ├── HousekeepingObserver.java
│   └── ReceptionObserver.java
├── security/
│   └── SecurityService.java (Singleton)
└── util/
    └── ReportGenerator.java
```

## 🚀 Como Executar

### Pré-requisitos
- Java JDK 11 ou superior
- IDE (IntelliJ IDEA, Eclipse, VS Code)

### Compilar e Executar

```bash
# Compilar
javac -d bin -sourcepath src src/app/HotelReservationSystem.java

# Executar
java -cp bin app.HotelReservationSystem
```

### Usando IDE
1. Abrir o projeto na sua IDE
2. Localizar a classe `HotelReservationSystem.java` no pacote `app`
3. Executar o método `main`

## 📊 Funcionalidades Implementadas

### Gestão de Reservas (FR-01 a FR-04)
- ✅ Criação de reservas
- ✅ Verificação de disponibilidade
- ✅ Prevenção de overbooking
- ✅ Modificação e cancelamento de reservas

### Preços Dinâmicos (FR-05 a FR-07)
- ✅ Cálculo automático de preços
- ✅ Ajuste baseado na ocupação
- ✅ Definição manual de regras

### Check-in/Check-out (FR-08 a FR-10)
- ✅ Check-in presencial
- ✅ Check-out com faturação
- ✅ Atualização automática do estado do quarto

### Atribuição de Quartos (FR-11 a FR-12)
- ✅ Atribuição automática
- ✅ Atribuição manual

### Housekeeping (FR-13 a FR-14)
- ✅ Consulta de estado dos quartos
- ✅ Gestão de listas de tarefas
- ✅ Notificações automáticas (Observer)

### Manutenção (FR-15 a FR-16)
- ✅ Registo de pedidos
- ✅ Acompanhamento e atualização

### Faturação e Pagamentos (FR-17 a FR-19)
- ✅ Emissão de faturas
- ✅ Processamento de pagamentos
- ✅ Histórico financeiro

### Relatórios (FR-20 a FR-21)
- ✅ Relatórios de ocupação
- ✅ Relatórios financeiros

## 🧪 Testes

O projeto está preparado para testes automatizados com JUnit. A estrutura modular e o uso de interfaces facilitam a criação de mocks e testes unitários.

### Áreas a testar (>30 testes recomendados):
- ReservationService (criação, modificação, cancelamento, overbooking)
- PricingService (cálculos, multiplicadores sazonais, ocupação)
- CheckInService e CheckOutService
- RoomService (disponibilidade, atualização de estado)
- SecurityService (autenticação, permissões)
- Observer Pattern (notificações)
- Factory Pattern (criação de utilizadores)
- InvoiceService (faturação, pagamentos)

## 👥 Autores

- Claúdio Lima: 230001164
- Duarte Dias: 210100372
- Gabriel Ribeiro: 230000982
- João Boieiro: 230001180

## 📝 Licença

Projeto académico - Instituto Politécnico de Santarém, 2025/2026

## 📚 Documentação Adicional

Para mais detalhes sobre requisitos, arquitetura e diagramas UML, consultar:
- Documento de Requisitos
- Documento de Arquitetura de Software
- Diagramas UML (Casos de Uso, Classes, Sequência, Atividade)
