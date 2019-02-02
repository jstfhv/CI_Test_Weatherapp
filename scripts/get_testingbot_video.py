#!/opt/pyenv/shims/python
import json, sys, urllib, os, time;

url = "https://" + os.environ['TESTINGBOT_CREDENTIALS'] + "@api.testingbot.com/v1/tests?count=10?skip_fields=logs,thumbs,steps"

# this loop is needed because the video may not be available right now and we need to wait
# we do 10 tries after we exit
for i in range(0, 10):
    jsonurl = urllib.urlopen(url)
    response = json.loads(jsonurl.read())

    # get the video link of the latest test with the name "User interaction with history"
    for test in response['data']:
        if test['name'] == 'User interaction with history (video)' and test['state'] == 'COMPLETE':
            if not test['assets_available']:
                # wait 5 seconds if the asset is not available and try again
                time.sleep(5)
                break

            print test['video']
            # download the Video
            urllib.urlretrieve (test['video'], "walk_through.mp4")
            sys.exit(0)

    if i >=9:
        print "retry limit reached"
        sys.exit(1)