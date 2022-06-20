TOP_DIR=.
README=$(TOP_DIR)/README.md
CUR_IP=$(shell ipconfig getifaddr en0)
VERSION=$(strip $(shell cat version))

include .makefiles/release.mk
.PHONY: clean build init lint test precommit travis-init travis travis-deploy run deploy test-did test-swap  test-create install uninstall test-setting
