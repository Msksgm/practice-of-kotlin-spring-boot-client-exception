.PHONY: fmt.main
fmt.main: ## main ソースセットの型解決を使用して format
	./gradlew detektMain --auto-correct

.PHONY: lint.main
lint.main: ## main ソースセットの型解決を使用して lint
	./gradlew detektMain

.PHONY: fmt.test
fmt.test: ## test ソースセットの型解決を使用して format
	./gradlew detektTest --auto-correct

.PHONY: lint.test
lint.test: ## test ソースセットの型解決を使用して lint
	./gradlew detektTest

################################################################################
# Utility-Command help
################################################################################
.DEFAULT_GOAL := help

################################################################################
# マクロ
################################################################################
# Makefileの中身を抽出してhelpとして1行で出す
# $(1): Makefile名
define help
  grep -E '^[\.a-zA-Z0-9_-]+:.*?## .*$$' $(1) \
  | grep --invert-match "## non-help" \
  | awk 'BEGIN {FS = ":.*?## "}; {printf "\033[36m%-20s\033[0m %s\n", $$1, $$2}'
endef

################################################################################
# タスク
################################################################################
.PHONY: help
help: ## Make タスク一覧
	@echo '######################################################################'
	@echo '# Makeタスク一覧'
	@echo '# $$ make XXX'
	@echo '# or'
	@echo '# $$ make XXX --dry-run'
	@echo '######################################################################'
	@echo $(MAKEFILE_LIST) \
	| tr ' ' '\n' \
	| xargs -I {included-makefile} $(call help,{included-makefile})
