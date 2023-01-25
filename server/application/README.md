# Application Layer

Enthält die Business Logic der Anwendung.
Hat nur Abhängigkeiten zu Domain-Layer.
Werden Services oder Datenbankzugriffe benötigt, wird ein Interface beschrieben und ein höherer Layer implementiert dieses und stellt die Implementierung mittels Dependency Injection zur Verfügung.
