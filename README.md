# Car Insurance Claim Settlement Demo

This repository brings together a complete AI-powered car insurance claim demo.

The goal is to turn a user-uploaded image into an automated claim workflow:

1. Detect whether the image contains a car.
2. Check whether the car is damaged.
3. Predict the damage location.
4. Predict the damage severity.

The solution was designed to be deployed in **CPD Cloud** as a set of independent services connected by a Web application.

## Overview

The system is made of several ML services and one main Web application:

- **Web Application**: user interface for uploading an image and displaying results.
- **carDetectionPredictionML**: detects whether the image contains a car.
- **carDamagePredictionML**: detects whether the car is damaged.
- **carDamageLocalizationPredictionML**: predicts where the damage is located.
- **carDamageSeverityPredictionML**: predicts how severe the damage is.
- **car-insurance-claim-settlement**: graphical demo that orchestrates the full claim journey.

## End-to-end workflow

The application follows this sequence:

1. The user opens the Web application.
2. An image is uploaded.
3. The car detection service checks whether the image contains a car.
4. If a car is detected, the damage detection service checks whether it is damaged.
5. If damage is detected, a localization model predicts the impact area.
6. A severity model predicts the seriousness of the damage.
7. The final result is displayed in the Web application and can be used in the claim process.

## Project roles

### car-insurance-claim-settlement
This is the main graphical application for the insurance claim demo.
It manages the user journey, receives the image, and displays the outputs from the ML services.

### carDetectionPredictionML
This project is a binary classification service.
It determines whether the submitted image contains a car or not.

### carDamagePredictionML
This project is also a binary classification service.
It determines whether the detected car is damaged.

### carDamageLocalizationPredictionML
This project is a multi-class classification service.
It predicts the damage location using the following labels:

- `0`: Front
- `1`: Rear
- `2`: Side

### carDamageSeverityPredictionML
This project is a multi-class classification service.
It predicts the damage severity using the following labels:

- `0`: Minor
- `1`: Moderate
- `2`: Severe

## CPD Cloud architecture

The solution was designed to run in **CPD Cloud** as a modular architecture.
Each ML model can be deployed as an independent API service and consumed by the main Web application.

### Deployed components

- **Web front end**: main application interface.
- **Car detection API**: ML endpoint for car presence detection.
- **Damage detection API**: ML endpoint for damage detection.
- **Damage localization API**: ML endpoint for impact location prediction.
- **Damage severity API**: ML endpoint for severity prediction.

## How the Web App and CPD services interact

- The Web application (Spring Controller) accepts an uploaded image, encodes it to base64 and builds a Claim JSON object which it POSTs to the CPD entry endpoint (property `cdp.rest.url`).  
- The CPD entry service (carDamagePredictionML) receives the Claim JSON, runs the damage prediction, appends its result to the JSON and conditionally calls the localization service.  
- The localization service appends its result and conditionally calls the severity service.  
- Each service appends its predictions into the same JSON structure and returns the enriched JSON to the caller. The final JSON is returned to the Web app and rendered in the `/claims/` view.

### Claim / Response JSON contract (recommended)
- Claim (sent by Web app):
  - id, image (base64), date, customerId, metadata (optional)
- Response (returned by services; example fields):
  - claimId
  - detected: {isCar: boolean, confidence: float}
  - damaged: {isDamaged: boolean, confidence: float}
  - localization: {label, class, confidence}
  - severity: {label, class, confidence}
  - timestamps: {...}
  - errors: [...]

### Deployment and configuration
- Deploy each ML project in CPD as an independent REST inference service. Use cpdctl or the CPD UI to create online deployments and obtain endpoint URLs and tokens [web:52].  
- Configure service URLs in application.properties (or environment variables), e.g.:
  - `cdp.rest.url=https://cpd.example.com/api/v1/entry-service`
  - `localization.service.url=https://cpd.example.com/api/v1/localization`
  - `severity.service.url=https://cpd.example.com/api/v1/severity`
- Secure services with tokens and enable timeouts and retries for inter-service calls.

### Best practices
- Keep the JSON schema stable across services.
- Append prediction fields rather than replacing them.
- Propagate errors in a structured `errors[]` field.
- Add timestamps/trace IDs to help debugging and latency measurement.

### Logical flow

```text
User
  ↓
Web Application
  ↓
Car detection API
  ↓
Damage detection API
  ↓
Damage localization API
  ↓
Damage severity API
  ↓
Displayed result
