# Import the required Modules
import speech_recognition as sr
import requests
import pyttsx3

URL = "https://cloud.boltiot.com/remote/<API_KEY>/digitalWrite?{}deviceName=BOLT1119308"


# Initialize the engine and recognizer
recognizer = sr.Recognizer()
engine = pyttsx3.init()

def main():
	
	# Listen to the user
	mic = sr.Microphone()
	with mic as source:
		print("Listening....")
		engine.say("What is your command")
		engine.runAndWait()

		recognizer.adjust_for_ambient_noise(source, duration=0.5)
		try:
			audio = recognizer.listen(source, timeout=3, phrase_time_limit=6)
			command = recognizer.recognize_google(audio, language='en-IN').lower().strip()
			print(command)
		except sr.WaitTimeoutError:
			write_and_speak('Timeout Error, Try again')
			main()
		except sr.UnknownValueError:
			write_and_speak('Cannot understand, Try again')
			main()
		except sr.RequestError:
			write_and_speak('No internet connection or Invalid Key')
			return

		# command for ON
		if 'turn on' in command or 'switch on' in command or 'open' in command:
			lookup_device(command, state='HIGH')

		# command for OFF
		elif 'turn off' in command or 'switch off' in command or 'close' in command:
			lookup_device(command, state='LOW')

		else:
			write_and_speak('Invalid command')



def write_and_speak(text: str):
	print(text)
	engine.say(text)
	engine.runAndWait()

def lookup_device(command, state):
	pins = {
		'light': 0,
		'fan': 1,
		'door': 2,
		'projector': 3,
		'curtains': 4
	}

	for device, pin in pins.items():
		if device in command:
			perform_action(device, pin, state)
			return

def perform_action(device, pin, state):
	assert state in ['HIGH', 'LOW']
	eq = {'HIGH':'On', 'LOW':'Off'}

	res = requests.get(URL.format(f'pin={pin}&state={state}&'))
	if res.json()['success'] == None:
		write_and_speak('The server is Offline')
	else:
		write_and_speak(f'Turning {eq[state]}  the {device}')

if __name__ == '__main__':
	main() 
