
TOKEN=$EMG_DATA_SDK
GIT_HASH=$(git rev-parse HEAD)

codecov --token="$TOKEN" --commit=$GIT_HASH --branch="develop"

